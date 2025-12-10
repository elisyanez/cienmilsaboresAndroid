package com.ipgan.cienmilsaboresandroid.model

import org.junit.Assert.*
import org.junit.Test

class ProductTest {

    @Test
    fun `creación de producto con todos los valores`() {
        // 1. Preparación (Arrange)
        val producto = Product(
            id = "TORTA001",
            name = "Torta de Chocolate",
            description = "Deliciosa torta de chocolate",
            category = "Tortas Circulares",
            imageUrl = "http://example.com/torta.jpg",
            price = 25.50,
            isVisible = true,
            stock = 10
        )

        // 2. Actuación (Act) - No se necesita, es una clase de datos

        // 3. Afirmación (Assert)
        assertEquals("TORTA001", producto.id)
        assertEquals("Torta de Chocolate", producto.name)
        assertEquals(25.50, (producto.price)?:0.0, 0.0)
        assertTrue(producto.isVisible!!)
        assertEquals(10, producto.stock)
    }

    @Test
    fun `creación de producto con valores nulos o por defecto`() {
        // 1. Preparación (Arrange)
        val producto = Product(
            id = "POSTRE002",
            name = "Flan Casero",
            description = null,
            category = "Postres Individuales",
            imageUrl = null,
            price = null,
            isVisible = false,
            stock = null
        )

        // 2. Actuación (Act) - No se necesita

        // 3. Afirmación (Assert)
        assertEquals("POSTRE002", producto.id)
        assertNull(producto.description)
        assertNull(producto.price)
        assertFalse(producto.isVisible!!)
        assertNull(producto.stock)
    }

    @Test
    fun `la función copy crea una copia con valores modificados`() {
        // 1. Preparación (Arrange)
        val productoOriginal = Product(id = "PROD01", name = "Original", price = 10.0, stock = 5, description = "desc", category = "cat", imageUrl = "url", isVisible = true)

        // 2. Actuación (Act)
        val productoCopiado = productoOriginal.copy(name = "Copia Modificada", stock = 3)

        // 3. Afirmación (Assert)
        assertEquals("PROD01", productoCopiado.id) // El ID no cambia
        assertEquals("Copia Modificada", productoCopiado.name) // El nombre sí
        assertEquals(10.0, (productoCopiado.price)?:0.0, 0.0) // El precio no cambia
        assertEquals(3, productoCopiado.stock) // El stock sí
        assertNotEquals(productoOriginal, productoCopiado)
    }
}