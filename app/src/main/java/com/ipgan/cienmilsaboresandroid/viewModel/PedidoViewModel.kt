package com.ipgan.cienmilsaboresandroid.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ipgan.cienmilsaboresandroid.model.CrearPedidoRequest
import com.ipgan.cienmilsaboresandroid.model.ItemCarritoRequest
import com.ipgan.cienmilsaboresandroid.model.Pedido
import com.ipgan.cienmilsaboresandroid.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PedidoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PedidoRepository(application.applicationContext)

    private val _pedidoCreado = MutableStateFlow<Pedido?>(null)
    val pedidoCreado: StateFlow<Pedido?> = _pedidoCreado.asStateFlow()

    private val _misPedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val misPedidos: StateFlow<List<Pedido>> = _misPedidos.asStateFlow()

    private val _todosLosPedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val todosLosPedidos: StateFlow<List<Pedido>> = _todosLosPedidos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // --- NUEVO: StateFlow para manejar errores ---
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun crearPedido(usuarioRun: String, items: Map<com.ipgan.cienmilsaboresandroid.model.Product, Int>) {
        viewModelScope.launch {
            _isLoading.value = true
            _pedidoCreado.value = null
            _error.value = null // Limpiamos errores anteriores

            // Verificación de precios nulos antes de enviar
            val hasNullPrice = items.keys.any { it.price == null }
            if (hasNullPrice) {
                _error.value = "Error: Uno de los productos en el carrito no tiene precio."
                _isLoading.value = false
                return@launch
            }

            try {
                val itemsCarritoRequest = items.map { (product, qty) ->
                    ItemCarritoRequest(
                        producto = product,
                        cantidad = qty
                    )
                }
                val request = CrearPedidoRequest(usuarioRun = usuarioRun, items = itemsCarritoRequest)
                val nuevoPedido = repository.crearPedido(request)

                if (nuevoPedido != null) {
                    _pedidoCreado.value = nuevoPedido
                } else {
                    _error.value = "El servidor rechazó el pedido. Revisa los datos."
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Error de conexión al crear el pedido."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarMisPedidos(usuarioRun: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _misPedidos.value = repository.obtenerPedidosPorUsuario(usuarioRun) ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                _misPedidos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarTodosLosPedidos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _todosLosPedidos.value = repository.obtenerTodosLosPedidos() ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                _todosLosPedidos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun limpiarPedidoCreado(){
        _pedidoCreado.value = null
    }

    // --- NUEVO: Función para limpiar el estado de error ---
    fun limpiarError() {
        _error.value = null
    }
}
