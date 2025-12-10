package com.ipgan.cienmilsaboresandroid.model

import com.google.gson.annotations.SerializedName

data class Product(
    // 1. CAMBIAMOS EL TIPO DE DATO A STRING
    @SerializedName("codigo")
    val id: String?,

    @SerializedName("nombre")
    val name: String?,

    @SerializedName("descripcion")
    val description: String?,

    @SerializedName("categoria")
    val category: String? = "",

    @SerializedName("imagenUrl")
    val imageUrl: String?,

    // 2. CAMBIAMOS EL PRECIO A DOUBLE PARA SOPORTAR DECIMALES
    @SerializedName("precio")
    val price: Double?,

    @SerializedName("visible")
    val isVisible: Boolean?,

    @SerializedName("stock")
    val stock: Int?
)
