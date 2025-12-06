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

    fun eliminarItems(product: Product) {
        if (_itemsCarrito.containsKey(product)) {
            _itemsCarrito.remove(product)
            calcularTotal()
        }
    }

    private fun calcularTotal() {
        _total.value = _itemsCarrito.entries.sumOf { (product, qty) -> (product.price * qty).toDouble() }
    }

    fun vaciarItems() {
        _itemsCarrito.clear()
        _total.value = 0.0
    }
}