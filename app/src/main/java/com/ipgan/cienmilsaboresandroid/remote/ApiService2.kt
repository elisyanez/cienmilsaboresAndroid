package com.ipgan.cienmilsaboresandroid.remote

import com.ipgan.cienmilsaboresandroid.model.LoginRequest
import com.ipgan.cienmilsaboresandroid.model.LoginResponse
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService2 {
    // --- USUARIOS ---
    @GET("/api/usuarios") // <--- CORREGIDO
    suspend fun getUsuarios(): Response<List<User>>

    @GET("/api/usuarios/{run}") // <--- CORREGIDO
    suspend fun getUsuarioByRun(@Path("run") run: String): User

    @POST("/api/usuarios") // <--- CORREGIDO
    suspend fun createUsuario(@Body usuario: User): Response<User>

    @PUT("/api/usuarios/{run}") // <--- CORREGIDO
    suspend fun updateUsuario(@Path("run") run: String, @Body usuario: User): Response<User>

    @DELETE("/api/usuarios/{run}") // <--- CORREGIDO
    suspend fun deleteUsuario(@Path("run") run: String): Response<Unit>

    // --- PRODUCTOS ---
    @GET("/api/productos") // <--- CORREGIDO
    suspend fun getProductos(): Response<List<Product>>

    @GET("/api/productos/{codigo}") // <--- CORREGIDO
    suspend fun getProductoByCodigo(@Path("codigo") codigo: Int): Product

    @POST("/api/productos") // <--- CORREGIDO
    suspend fun createProducto(@Body product: Product): Response<Product>

    @PUT("/api/productos/{codigo}") // <--- CORREGIDO
    suspend fun updateProducto(@Path("codigo") codigo: Int, @Body product: Product): Response<Product>

    @DELETE("/api/productos/{codigo}") // <--- CORREGIDO
    suspend fun deleteProducto(@Path("codigo") codigo: Int): Response<Unit>

    // --- TEST ---
    @GET("/api/test/ping") // <--- CORREGIDO
    suspend fun testPing(): Response<String>

    // LOGIN
    @POST("/api/auth/login") // La nueva ruta de autenticación
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/api/usuarios/me") // <--- AÑADE ESTE ENDPOINT
    suspend fun getMyProfile(): Response<User>

}
