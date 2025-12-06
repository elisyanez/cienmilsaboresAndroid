package com.ipgan.cienmilsaboresandroid.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id_prod")
    val id: Int,

    @SerializedName("nom_prod")
    val name: String,

    @SerializedName("desc_prod")
    val description: String,

    @SerializedName("categoria_prod")
    val category: String = "",

    @SerializedName("imagen_prod")
    val imageUrl: String,

    @SerializedName("precio_prod")
    val price: Int,

    @SerializedName("visible_prod")
    val isVisible: Boolean,

    @SerializedName("stock_prod")
    val stock: Int
)
