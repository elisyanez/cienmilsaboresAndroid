package com.ipgan.cienmilsaboresandroid.repository

import android.content.Context
import com.ipgan.cienmilsaboresandroid.model.ActualizarEstadoRequest
import com.ipgan.cienmilsaboresandroid.model.CrearPedidoRequest
import com.ipgan.cienmilsaboresandroid.model.EstadoPedido
import com.ipgan.cienmilsaboresandroid.model.Pedido
import com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance2

class PedidoRepository(context: Context) {
    private val apiService = RetrofitInstance2.getApi(context)

    suspend fun crearPedido(request: CrearPedidoRequest): Pedido? {
        val response = apiService.crearPedido(request)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun obtenerPedidosPorUsuario(usuarioRun: String): List<Pedido>? {
        val response = apiService.obtenerPedidosUsuario(usuarioRun)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun obtenerTodosLosPedidos(): List<Pedido>? {
        val response = apiService.obtenerTodosPedidos()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun actualizarEstado(pedidoId: Long, nuevoEstado: EstadoPedido): Pedido? {
        val request = ActualizarEstadoRequest(nuevoEstado)
        val response = apiService.actualizarEstadoPedido(pedidoId, request)
        return if (response.isSuccessful) response.body() else null
    }
}
