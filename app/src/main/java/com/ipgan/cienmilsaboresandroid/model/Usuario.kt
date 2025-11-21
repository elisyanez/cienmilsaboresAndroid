package com.ipgan.cienmilsaboresandroid.model

import java.util.Date

data class Usuario(
    val run_usu: String,
    val nom_usu: String,
    val apell_usu: String,
    val email_usu: String,
    val cumple_usu: Date = Date(),
    val tel_usu: String = "",
    val contra_usu: String,
    val duoc_usu: Boolean = false,
    val tipo_usu: String = "Cliente",
    val foto_usu: String = "",
    val region_usu: String = "",
    val comuna_usu: String = "",
    val direccion_usu: String
)