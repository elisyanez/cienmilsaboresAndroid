package com.ipgan.cienmilsaboresandroid.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos del Usuario, sincronizado con la entidad 'Usuario' del backend.
 * Las anotaciones @SerializedName son CRUCIALES para que Gson mapee
 * correctamente el JSON del backend a las propiedades de esta clase.
 */
data class User(
    @SerializedName("run")
    val run: String,

    // Le decimos a Gson que el campo "nombre" del JSON debe ir en la variable "name" de Kotlin.
    @SerializedName("nombre")
    val name: String,

    // Le decimos a Gson que el campo "apellidos" del JSON debe ir en la variable "lastName".
    @SerializedName("apellidos")
    val lastName: String?,

    // Y así con todos los demás campos...
    @SerializedName("correo")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("direccion")
    val address: String?,

    @SerializedName("role")
    val role: String,

    @SerializedName("region")
    val region: String?,

    @SerializedName("comuna")
    val commune: String?,
)
