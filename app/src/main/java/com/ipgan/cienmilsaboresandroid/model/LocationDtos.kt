package com.ipgan.cienmilsaboresandroid.model

import com.google.gson.annotations.SerializedName

data class RegionDTO(
    @SerializedName("codigo")
    val codigo: String,
    @SerializedName("nombre")
    val nombre: String
)

data class ComunaDTO(
    @SerializedName("codigo")
    val codigo: String,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("regionCodigo")
    val regionCodigo: String,
    @SerializedName("regionNombre")
    val regionNombre: String
)
