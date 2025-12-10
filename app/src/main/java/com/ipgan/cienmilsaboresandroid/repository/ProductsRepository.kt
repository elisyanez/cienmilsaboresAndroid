package com.ipgan.cienmilsaboresandroid.repository

import android.content.Context
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance2

class ProductsRepository(context: Context) {
    private val apiService = RetrofitInstance2.getApi(context)

    suspend fun getProductos(): List<Product>? {
        val response = apiService.getProductos()
        return if (response.isSuccessful) response.body()
        else null
    }

    // ¡CAMBIO! El código es ahora un String
    suspend fun getProductoByCodigo(codigo: String): Product? {
        val response = apiService.getProductoByCodigo(codigo)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun crearProducto(product: Product): Boolean {
        val response = apiService.createProducto(product)
        return response.isSuccessful
    }

    // ¡CAMBIO! El código es ahora un String
    suspend fun eliminarProducto(codigo: String): Boolean {
        val response = apiService.deleteProducto(codigo)
        return response.isSuccessful
    }

    // ¡CAMBIO! El código es ahora un String
    suspend fun updateProducto(codigo: String, product: Product): Boolean {
        val response = apiService.updateProducto(codigo, product)
        return response.isSuccessful
    }

    suspend fun testPing(): Boolean {
        return try {
            val response = apiService.testPing()
            response.isSuccessful && response.body() != null
        } catch (e: Exception) {
            false
        }
    }
}
