package com.ipgan.cienmilsaboresandroid.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos unificado para el Usuario.
 * Contiene todos los campos necesarios para registro, login, y gestión de administradores.
 * Los campos extra son opcionales para mantener la compatibilidad con el registro simple.
 */
data class User(
    @SerializedName("id_usu")
    val id: Int? = null,

    @SerializedName("nom_usu")
    val name: String,

    @SerializedName("apell_usu")
    val lastName: String? = null,

    @SerializedName("run_usu")
    val run: String,

    @SerializedName("email_usu")
    val email: String,

    @SerializedName("direccion_usu")
    val address: String,

    @SerializedName("contra_usu")
    val password: String,

    @SerializedName("tipo_usu")
    val role: String = "cliente",

    @SerializedName("regio_usu")
    val region: String? = null,

    @SerializedName("comuna_usu")
    val commune: String? = null,

    @SerializedName("foto_usu")
    val photoUrl: String? = null
)
