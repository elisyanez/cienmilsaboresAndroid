package com.ipgan.cienmilsaboresandroid.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ipgan.cienmilsaboresandroid.config.TokenManager
import com.ipgan.cienmilsaboresandroid.model.*
import com.ipgan.cienmilsaboresandroid.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuarioRepository(application.applicationContext)

    // --- ESTADO DEL USUARIO ---
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    // --- ESTADO DE REGIONES Y COMUNAS ---
    private val _regiones = MutableStateFlow<List<RegionDTO>>(emptyList())
    val regiones: StateFlow<List<RegionDTO>> = _regiones.asStateFlow()

    private val _comunas = MutableStateFlow<List<ComunaDTO>>(emptyList())
    val comunas: StateFlow<List<ComunaDTO>> = _comunas.asStateFlow()

    init {
        // Cargamos las regiones y comunas al iniciar el ViewModel
        loadRegionesYComunas()
    }

    private fun loadRegionesYComunas() {
        viewModelScope.launch {
            _regiones.value = repository.getRegiones() ?: emptyList()
            _comunas.value = repository.getComunas() ?: emptyList()
        }
    }

    suspend fun login(email: String, pass: String): Boolean {
        return try {
            val request = LoginRequest(correo = email, password = pass)
            val loginResponse = repository.login(request)

            if (loginResponse.isSuccessful && loginResponse.body() != null) {
                val responseBody = loginResponse.body()!!
                TokenManager.saveToken(getApplication(), responseBody.token)
                com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance2.invalidate()

                val loggedInUser = User(
                    run = responseBody.run,
                    name = responseBody.name,
                    lastName = responseBody.lastName,
                    email = responseBody.email,
                    password = "", // La contraseña no se guarda en el estado
                    address = responseBody.address,
                    role = responseBody.role,
                    region = responseBody.region,
                    commune = responseBody.commune
                )
                _user.value = loggedInUser
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun register(user: User): Boolean {
        return repository.saveUsuario(user)
    }

    fun logout() {
        _user.value = null
        TokenManager.clearToken(getApplication())
        com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance2.invalidate()
    }

    suspend fun updateUser(updatedUser: User) {
        _user.value?.let { currentUser ->
            val run = currentUser.run
            if (repository.updateUsuario(run, updatedUser)) {
                // Actualizamos el estado con la información completa
                // que viene del formulario.
                _user.value = updatedUser
            }
        }
    }
}
