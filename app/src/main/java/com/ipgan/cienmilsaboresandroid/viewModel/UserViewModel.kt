package com.ipgan.cienmilsaboresandroid.viewModel

import androidx.lifecycle.ViewModel
import com.ipgan.cienmilsaboresandroid.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    // Esta es la lista en memoria para guardar los usuarios. En una app real, esto sería una base de datos.
    private val registeredUsers = mutableListOf<User>()

    // El usuario que está logueado. Si es nulo, nadie ha iniciado sesión.
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        // Para probar, vamos a registrar un usuario de antemano.
        registeredUsers.add(User(email = "admin@local", password = "password", name = "Admin", address = "Local"))
    }

    /**
     * Para registrar un nuevo usuario.
     * @return Devuelve verdadero si se registra bien, falso si el usuario ya existe.
     */
    fun register(user: User): Boolean {
        // Reviso si el usuario ya existe.
        if (registeredUsers.any { it.email == user.email }) {
            return false // El usuario con este correo ya existe.
        }
        registeredUsers.add(user)
        return true
    }

    /**
     * Para intentar loguear un usuario, revisando la lista de registrados.
     * @return Verdadero si se loguea bien, si no, falso.
     */
    fun login(email: String, pass: String): Boolean {
        val foundUser = registeredUsers.find { it.email == email && it.password == pass }
        _user.value = foundUser
        return foundUser != null
    }

    /**
     * Para cerrar la sesión del usuario actual.
     */
    fun logout() {
        _user.value = null
    }

    /**
     * Para actualizar el perfil del usuario que está logueado.
     */
    fun updateUser(updatedUser: User) {
        // El usuario tiene que estar logueado para poder actualizar su perfil.
        _user.value?.let { currentUser ->
            val index = registeredUsers.indexOfFirst { u -> u.email == currentUser.email }
            if (index != -1) {
                // Actualizamos el usuario en la lista y también el estado del usuario logueado.
                registeredUsers[index] = updatedUser
                _user.value = updatedUser
            }
        }
    }
}
