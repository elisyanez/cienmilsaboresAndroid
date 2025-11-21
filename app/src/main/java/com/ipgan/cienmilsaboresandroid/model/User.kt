package com.ipgan.cienmilsaboresandroid.model

/**
 * Esta es nuestra data class para representar el modelo de un usuario en la aplicación.
 */
data class User(
    val name: String,
    val email: String,
    val address: String,
    val password: String // Lo necesitamos para que funcione el login
)
