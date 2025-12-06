package com.ipgan.cienmilsaboresandroid.repository

import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance2

class ProductsRepository {
    private val apiService = RetrofitInstance2.api

    suspend fun getProductos(): List<Product>? {
        val response = apiService.getProductos()
        return if (response.isSuccessful) response.body()
        else null
    }

    suspend fun getProductoByCodigo(codigo: Int): Product? {
        val response = apiService.getProductoByCodigo(codigo)
        return if (response != null) response
        else null
    }

    suspend fun crearProducto(product: Product): Boolean {
        val response = apiService.createProducto(product)
        return if (response != null) true
        else false
    }

    suspend fun eliminarProducto(codigo: Int):Boolean{
        val response = apiService.deleteProducto(codigo)
        return response.isSuccessful
    }

    suspend fun updateProducto(codigo: Int, product: Product): Boolean {
        val response = apiService.updateProducto(codigo, product)
        return response.isSuccessful
    }
}