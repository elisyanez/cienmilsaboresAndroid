package com.ipgan.cienmilsaboresandroid.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipgan.cienmilsaboresandroid.model.User
import com.ipgan.cienmilsaboresandroid.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UsuarioRepository()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    // 1. CAMBIAMOS LA FUNCIÓN PARA QUE SEA 'suspend' Y DEVUELVA UN USUARIO O NULO
    /**
     * Intenta loguear un usuario usando el repositorio.
     * Esta función es 'suspend' porque necesita esperar la respuesta de la API.
     * @return Devuelve el objeto User si el login es exitoso, o null si falla.
     */
    suspend fun login(email: String, pass: String): User? {
        val userList = repository.getUsuarios()
        val foundUser = userList?.find { it.email == email && it.password == pass }

        // Si encontramos al usuario, lo guardamos en nuestro estado.
        _user.value = foundUser
        return foundUser
    }

    /**
     * Para registrar un nuevo usuario usando la API.
     * La función ahora es 'suspend' porque la operación de red toma tiempo.
     * @return Devuelve verdadero si se registra bien, falso si no.
     */
    suspend fun register(user: User): Boolean {
        return repository.saveUsuario(user)
    }

    /**
     * Para cerrar la sesión del usuario actual.
     */
    fun logout() {
        _user.value = null
    }

    /**
     * Para actualizar el perfil del usuario que está logueado.
     * La función ahora es 'suspend' para reflejar la llamada a la API.
     */
    suspend fun updateUser(updatedUser: User) {
        _user.value?.let { currentUser ->
            val run = currentUser.run
            if (repository.updateUsuario(run, updatedUser)) {
                _user.value = updatedUser
            }
        }
    }
}
