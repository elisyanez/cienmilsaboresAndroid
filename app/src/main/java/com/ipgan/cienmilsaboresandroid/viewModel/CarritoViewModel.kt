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
        // Aumenta la cantidad del producto en el carrito.
        _itemsCarrito[product] = (_itemsCarrito[product] ?: 0) + 1
        calcularTotal()
    }

    fun eliminarItems(product: Product) {
        // Elimina completamente un producto del carrito.
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
        // Recalcula el total sumando el precio de cada producto por su cantidad.
        // La lógica ahora usa el precio como Double, que es más seguro.
        _total.value = _itemsCarrito.entries.sumOf { (product, qty) ->
            (product.price ?: 0.0) * qty
        }
    }
}
