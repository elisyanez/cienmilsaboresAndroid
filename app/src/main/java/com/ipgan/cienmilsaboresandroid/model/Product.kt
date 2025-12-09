package com.ipgan.cienmilsaboresandroid.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("codigo")
    val id: Int?,

    @SerializedName("nombre")
    val name: String?,

    @SerializedName("descripcion")
    val description: String?,

    @SerializedName("categoria")
    val category: String? = "",

    @SerializedName("imagenUrl")
    val imageUrl: String?,

    @SerializedName("precio")
    val price: Int?,

    @SerializedName("visible")
    val isVisible: Boolean?,

    @SerializedName("stock")
    val stock: Int?
)
