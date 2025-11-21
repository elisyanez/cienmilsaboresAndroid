package com.ipgan.cienmilsaboresandroid.data

import androidx.compose.runtime.mutableStateMapOf
import com.ipgan.cienmilsaboresandroid.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class ProductRepositoryFake {

    // Aquí defino la lista de productos que vamos a mostrar. Por ahora son datos fijos.
    private val _products = listOf(
        Product(1, "Torta de Chocolate", "Deliciosa torta con cobertura de chocolate", 15000, 10),
        Product(2, "Kuchen de Manzana", "Clásico kuchen sureño con manzanas frescas", 12000, 15),
        Product(3, "Cheesecake de Frambuesa", "Suave cheesecake con mermelada de frambuesa casera", 18000, 8)
    )

    // Este mapa va a guardar los productos que vaya agregando al carrito.
    private val _cart = mutableStateMapOf<Product, Int>()
    // Expongo el carrito para que la UI pueda observarlo y pintarse cuando cambie.
    val cart: Map<Product, Int> = _cart

    // Una función simple para obtener todos mis productos.
    fun getProducts(): List<Product> = _products

    /**
     * Esta función me sirve para buscar un producto por su ID.
     * Lo envuelvo en un Flow para que parezca que es una llamada asíncrona, como si viniera de una API.
     */
    fun getProductById(productId: Int): Flow<Product?> {
        val product = _products.find { it.id_prod == productId }
        return flowOf(product)
    }

    // Con esta función agrego un producto al carrito. Si ya existe, le sumo 1 a la cantidad.
    fun addToCart(product: Product) {
        _cart[product] = (_cart[product] ?: 0) + 1
    }

    // Para quitar productos del carrito. Si la cantidad es mayor a 1, solo resto. Si es 1, lo elimino.
    fun removeFromCart(product: Product) {
        val currentQty = _cart[product] ?: 0
        if (currentQty > 1) {
            _cart[product] = currentQty - 1
        } else {
            _cart.remove(product)
        }
    }

    // Esta función es para dejar el carrito completamente vacío.
    fun clearCart() {
        _cart.clear()
    }
}
