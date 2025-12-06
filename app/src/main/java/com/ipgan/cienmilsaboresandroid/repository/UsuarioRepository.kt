package com.ipgan.cienmilsaboresandroid.repository

import com.ipgan.cienmilsaboresandroid.model.User
import com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance2

class UsuarioRepository {
    private val apiService = RetrofitInstance2.api

    suspend fun getUsuarios(): List<User>? {
        val response = apiService.getUsuarios()
        return if (response.isSuccessful) response.body()
        else null
    }

    suspend fun getUsuarioRun(run: String): User? {
        val response = apiService.getUsuarioByRun(run)
        return if (response != null) response
        else null
    }

    suspend fun saveUsuario(usuario: User): Boolean {
        val response = apiService.createUsuario(usuario)
        return if (response != null) true
        else false
    }

    suspend fun eliminarUsuario(run: String):Boolean{
        val response = apiService.deleteUsuario(run)
        return response.isSuccessful
    }

    suspend fun updateUsuario(run: String, usuario: User): Boolean {
        val response = apiService.updateUsuario(run, usuario)
        return response.isSuccessful
    }
}