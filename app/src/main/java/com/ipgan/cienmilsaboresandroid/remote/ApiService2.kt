package com.ipgan.cienmilsaboresandroid.remote

import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService2 {
    //USUARIOS
    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<User>>
    //suspend hace que la función se ejecute en un hilo separado,
    // es decir que tenga que esperar a que se complete la petición antes de continuar

    @GET("usuarios/{run}")
    suspend fun getUsuarioByRun(@Path("run") run: String): User

    @POST("usuarios")
    suspend fun createUsuario(@Body usuario: User): Response<User>

    @PUT("usuarios/{run}")
    suspend fun updateUsuario(@Path("run") run: String, @Body usuario: User): Response<User>

    @DELETE("usuarios/{run}")
    suspend fun deleteUsuario(@Path("run") run: String): Response<Unit>

    //USUARIOS
    @GET("productos")
    suspend fun getProductos(): Response<List<Product>>

    @GET("productos/{codigo}")
    suspend fun getProductoByCodigo(@Path("codigo") codigo: Int): Product

    @POST("productos")
    suspend fun createProducto(@Body product: Product): Response<Product>

    @PUT("productos/{codigo}")
    suspend fun updateProducto(@Path("codigo") codigo: Int, @Body product: Product): Response<Product>

    @DELETE("productos/{codigo}")
    suspend fun deleteProducto(@Path("codigo") codigo: Int): Response<Unit>

}