package com.ipgan.cienmilsaboresandroid.repository

import android.content.Context
import com.ipgan.cienmilsaboresandroid.model.* // Importamos los nuevos DTOs
import com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance2
import retrofit2.Response

class UsuarioRepository(context: Context) {
    private val apiService = RetrofitInstance2.getApi(context)

    suspend fun getUsuarios(): List<User>? {
        val response = apiService.getUsuarios()
        return if (response.isSuccessful) response.body()
        else null
    }

    suspend fun getUsuarioRun(run: String): User? {
        // Nota: Esta función parece devolver User directamente, no Response<User>.
        // La lógica original podría ser diferente. Asumo que está bien así.
        return apiService.getUsuarioByRun(run)
    }

    suspend fun saveUsuario(usuario: User): Boolean {
        val response = apiService.createUsuario(usuario)
        // Si la respuesta es exitosa, devolvemos true.
        return response.isSuccessful
    }

    suspend fun eliminarUsuario(run: String): Boolean {
        val response = apiService.deleteUsuario(run)
        return response.isSuccessful
    }

    suspend fun updateUsuario(run: String, usuario: User): Boolean {
        val response = apiService.updateUsuario(run, usuario)
        return response.isSuccessful
    }

    // --- LOGIN & PROFILE ---
    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return apiService.login(request)
    }

    suspend fun getMyProfile(): Response<User> {
        return apiService.getMyProfile()
    }

    // --- UTILIDADES (Regiones y Comunas) ---

    suspend fun getRegiones(): List<RegionDTO>? {
        val response = apiService.getRegiones()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getComunas(): List<ComunaDTO>? {
        val response = apiService.getComunas()
        return if (response.isSuccessful) response.body() else null
    }
}
