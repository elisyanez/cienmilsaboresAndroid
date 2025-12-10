package com.ipgan.cienmilsaboresandroid.remote

import com.ipgan.cienmilsaboresandroid.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService2 {
    // --- USUARIOS ---
    @GET("/api/usuarios")
    suspend fun getUsuarios(): Response<List<User>>

    @GET("/api/usuarios/{run}")
    suspend fun getUsuarioByRun(@Path("run") run: String): User

    @POST("/api/usuarios")
    suspend fun createUsuario(@Body usuario: User): Response<User>

    @PUT("/api/usuarios/{run}")
    suspend fun updateUsuario(@Path("run") run: String, @Body usuario: User): Response<User>

    @DELETE("/api/usuarios/{run}")
    suspend fun deleteUsuario(@Path("run") run: String): Response<Unit>

    @GET("/api/usuarios/me")
    suspend fun getMyProfile(): Response<User>

    // --- PRODUCTOS ---
    @GET("/api/productos")
    suspend fun getProductos(): Response<List<Product>>

    @GET("/api/productos/{codigo}")
    suspend fun getProductoByCodigo(@Path("codigo") codigo: String): Response<Product>

    @POST("/api/productos")
    suspend fun createProducto(@Body product: Product): Response<Product>

    @PUT("/api/productos/{codigo}")
    suspend fun updateProducto(@Path("codigo") codigo: String, @Body product: Product): Response<Product>

    @DELETE("/api/productos/{codigo}")
    suspend fun deleteProducto(@Path("codigo") codigo: String): Response<Unit>

    // --- PEDIDOS ---
    @POST("/api/pedidos")
    suspend fun crearPedido(@Body request: CrearPedidoRequest): Response<Pedido>

    @GET("/api/pedidos/usuario/{usuarioRun}")
    suspend fun obtenerPedidosUsuario(@Path("usuarioRun") usuarioRun: String): Response<List<Pedido>>

    @GET("/api/pedidos")
    suspend fun obtenerTodosPedidos(): Response<List<Pedido>>

    @PUT("/api/pedidos/{pedidoId}/estado")
    suspend fun actualizarEstadoPedido(
        @Path("pedidoId") pedidoId: Long,
        @Body request: ActualizarEstadoRequest
    ): Response<Pedido>

    // --- LOGIN ---
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // --- UTILIDADES ---
    @GET("/api/util/regiones")
    suspend fun getRegiones(): Response<List<RegionDTO>>

    @GET("/api/util/comunas")
    suspend fun getComunas(): Response<List<ComunaDTO>>

    // --- TEST ---
    @GET("/api/test/ping")
    suspend fun testPing(): Response<String>
}