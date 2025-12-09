package com.ipgan.cienmilsaboresandroid.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ipgan.cienmilsaboresandroid.config.TokenManager
import com.ipgan.cienmilsaboresandroid.model.LoginRequest
import com.ipgan.cienmilsaboresandroid.model.User
import com.ipgan.cienmilsaboresandroid.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuarioRepository(application.applicationContext)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    suspend fun login(email: String, pass: String): Boolean {
        try {
            val request = LoginRequest(correo = email, password = pass)

            // 1. Llamamos al endpoint de login.
            // La respuesta (`LoginResponse`) ya contiene el token Y los datos del usuario.
            val loginResponse = repository.login(request)

            if (loginResponse.isSuccessful && loginResponse.body() != null) {
                val responseBody = loginResponse.body()!!

                // 2. Guardamos el token que vino en la respuesta.
                // Asegúrate que tu LoginResponse tiene un campo `token` o `jwt`.
                TokenManager.saveToken(getApplication(), responseBody.token)

                // 3. Invalidamos Retrofit para futuras llamadas.
                com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance2.invalidate()

                // 4. ¡AQUÍ ESTÁ EL CAMBIO CLAVE!
                // Creamos el objeto `User` directamente desde la respuesta del login,
                // sin necesidad de hacer otra llamada a la API.
                val loggedInUser = User(
                    run = responseBody.run,
                    name = responseBody.name,
                    lastName = responseBody.lastName,
                    email = responseBody.email,
                    password = "", // La contraseña nunca se guarda en el estado
                    address = responseBody.address,
                    role = responseBody.role,
                    // Asegúrate de que los nombres de las propiedades coincidan
                    // con los de tu LoginResponse (regionNombre, comunaNombre, etc.).
                    region = responseBody.region,
                    commune = responseBody.commune
                )

                // 5. ACTUALIZAMOS EL ESTADO
                // Este cambio hará que `LoginScreen` y `HomeScreen` reaccionen.
                _user.value = loggedInUser
                return true // ¡Éxito!
            }

            // Si el login falla, devolvemos false.
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    // El resto de las funciones (register, logout, updateUser) se mantienen igual.

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
                _user.value = updatedUser
            }
        }
    }
}
