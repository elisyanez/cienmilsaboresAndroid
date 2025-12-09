package com.ipgan.cienmilsaboresandroid.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val correo: String,
    val password: String)
data class LoginResponse(
    @SerializedName("token")
    val token: String,

    @SerializedName("run")
    val run: String,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("apellidos")
    val lastName: String?,

    @SerializedName("correo")
    val email: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("direccion")
    val address: String?,

    // Usaremos los nombres de las regiones/comunas, no los códigos
    @SerializedName("regionNombre")
    val region: String?,

    @SerializedName("comunaNombre")
    val commune: String?)
