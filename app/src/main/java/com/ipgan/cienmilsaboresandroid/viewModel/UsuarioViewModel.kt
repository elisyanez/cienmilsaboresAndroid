package com.ipgan.cienmilsaboresandroid.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ipgan.cienmilsaboresandroid.model.Usuario
import androidx.compose.runtime.*
import java.util.Date

class UsuarioViewModel: ViewModel() {
    var usuarioActivo by mutableStateOf<Usuario?>(null)
        private set

    fun login(email: String, password: String) {
        //SIMULAMOS UN USUARIO EXISTENTE
        val usuario = Usuario(
            run_usu = "12345678-9",
            nom_usu = "Elias",
            apell_usu = "Gutierrez",
            email_usu = email,
            contra_usu = password,
            duoc_usu = true,
            direccion_usu = "Av Siempre Viva 123",
            comuna_usu = "Santiago",
            region_usu = "RM",
            cumple_usu = Date(1950,12,29)
        )
        usuarioActivo = usuario
    }

    fun logout() {
        usuarioActivo = null

    }

    fun calcularDescuento(): Double {
        val usuario = usuarioActivo ?: return 0.0
        val edad = Date().year - usuario.cumple_usu.year
        if (edad > 50) {
            return 0.5
        }
        return 0.0

    }

}