package com.ipgan.cienmilsaboresandroid.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ipgan.cienmilsaboresandroid.model.Product

class CarritoViewModel : ViewModel() {
    private val _itemsCarrito = mutableStateMapOf<Product, Int>()
    val itemsCarrito: Map<Product, Int> = _itemsCarrito

    private val _total = mutableStateOf(0.0)
    val total: State<Double> = _total

    fun agregarItems(product: Product) {
        _itemsCarrito[product] = (_itemsCarrito[product] ?: 0) + 1
        calcularTotal()
    }

    // --- NUEVA FUNCIÓN PARA RESTAR UNIDADES ---
    fun restarItems(product: Product) {
        val currentQty = _itemsCarrito[product]
        if (currentQty != null) {
            if (currentQty > 1) {
                _itemsCarrito[product] = currentQty - 1
            } else {
                // Si la cantidad es 1, eliminamos el producto del carrito
                _itemsCarrito.remove(product)
            }
            calcularTotal()
        }
    }

    fun eliminarItems(product: Product) {
        if (_itemsCarrito.containsKey(product)) {
            _itemsCarrito.remove(product)
            calcularTotal()
        }
    }

    fun vaciarItems() {
        _itemsCarrito.clear()
        _total.value = 0.0
    }

    private fun calcularTotal() {
        _total.value = _itemsCarrito.entries.sumOf { (product, qty) ->
            (product.price ?: 0.0) * qty
        }
    }
}
