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

    suspend fun testPing(): Boolean {
        return try {
            val response = apiService.testPing()
            // Suponiendo que testPing() devuelve una Response<String> o similar
            response.isSuccessful && response.body() != null
        } catch (e: Exception) {
            false
        }
    }
}