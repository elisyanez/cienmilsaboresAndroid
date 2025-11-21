package com.ipgan.cienmilsaboresandroid.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ipgan.cienmilsaboresandroid.model.Product

class CarritoViewModel: ViewModel() {
    val itemsCarrito = mutableListOf<Product>()
    val total= mutableStateOf(0.0)

    fun agregarItems(product: Product) {
        itemsCarrito.add(product)
        calcularTotal()
    }
    fun eliminarItems(product: Product) {
        itemsCarrito.remove(product)
        calcularTotal()
    }
    private fun calcularTotal() {
        total.value = itemsCarrito.sumOf { it.precio_prod }.toDouble()
    }

    fun vaciarItems() {
        itemsCarrito.clear()
        total.value = 0.0
    }
}
