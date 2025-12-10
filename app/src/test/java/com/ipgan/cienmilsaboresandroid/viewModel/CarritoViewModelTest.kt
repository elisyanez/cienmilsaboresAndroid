package com.ipgan.cienmilsaboresandroid.viewModel

import com.ipgan.cienmilsaboresandroid.model.Product
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CarritoViewModelTest {

    private lateinit var viewModel: CarritoViewModel

    // Productos de prueba
    private val producto1 = Product("PROD1", "Torta", "desc","Torta","url", 15000.0,  true, 10)
    private val producto2 = Product("PROD2", "Tiramisu", "desc","Torta","url", 20000.0,  false, 30)

    @Before
    fun setup() {
        // Inicializar un nuevo ViewModel antes de cada prueba para asegurar el aislamiento
        viewModel = CarritoViewModel()
    }

    @Test
    fun `agregarItems - añade un nuevo producto y actualiza el total`() {
        // 1. Actuación (Act)
        viewModel.agregarItems(producto1)

        // 2. Afirmación (Assert)
        assertEquals(1, viewModel.itemsCarrito.size)
        assertTrue(viewModel.itemsCarrito.containsKey(producto1))
        assertEquals(1, viewModel.itemsCarrito[producto1])
        assertEquals(15.0, viewModel.total.value, 0.0)
    }

    @Test
    fun `agregarItems - aumenta la cantidad si el producto ya existe`() {
        // 1. Preparación (Arrange)
        viewModel.agregarItems(producto1) // Se añade una vez

        // 2. Actuación (Act)
        viewModel.agregarItems(producto1) // Se añade por segunda vez

        // 3. Afirmación (Assert)
        assertEquals(1, viewModel.itemsCarrito.size) // El tamaño del mapa no debe cambiar
        assertEquals(2, viewModel.itemsCarrito[producto1]) // La cantidad debe ser 2
        assertEquals(30.0, viewModel.total.value, 0.0) // El total debe ser el doble
    }

    @Test
    fun `eliminarItems - elimina un producto del carrito y recalcula el total`() {
        // 1. Preparación (Arrange)
        viewModel.agregarItems(producto1)
        viewModel.agregarItems(producto2)

        // 2. Actuación (Act)
        viewModel.eliminarItems(producto1)

        // 3. Afirmación (Assert)
        assertEquals(1, viewModel.itemsCarrito.size) // Solo debe quedar un producto
        assertFalse(viewModel.itemsCarrito.containsKey(producto1)) // El producto 1 no debe estar
        assertTrue(viewModel.itemsCarrito.containsKey(producto2)) // El producto 2 sí debe estar
        assertEquals(5.0, viewModel.total.value, 0.0) // El total debe ser solo el del producto 2
    }

    @Test
    fun `restarItems - reduce la cantidad de un producto`() {
        // 1. Preparación (Arrange)
        viewModel.agregarItems(producto1) // cantidad = 1
        viewModel.agregarItems(producto1) // cantidad = 2

        // 2. Actuación (Act)
        viewModel.restarItems(producto1)

        // 3. Afirmación (Assert)
        assertEquals(1, viewModel.itemsCarrito[producto1])
        assertEquals(15.0, viewModel.total.value, 0.0)
    }

    @Test
    fun `restarItems - elimina el producto si la cantidad es 1`() {
        // 1. Preparación (Arrange)
        viewModel.agregarItems(producto1)
        viewModel.agregarItems(producto2)

        // 2. Actuación (Act)
        viewModel.restarItems(producto1)

        // 3. Afirmación (Assert)
        assertFalse(viewModel.itemsCarrito.containsKey(producto1))
        assertEquals(1, viewModel.itemsCarrito.size)
        assertEquals(5.0, viewModel.total.value, 0.0)
    }

    @Test
    fun `vaciarItems - limpia el carrito completamente`() {
        // 1. Preparación (Arrange)
        viewModel.agregarItems(producto1)
        viewModel.agregarItems(producto2)

        // 2. Actuación (Act)
        viewModel.vaciarItems()

        // 3. Afirmación (Assert)
        assertTrue(viewModel.itemsCarrito.isEmpty())
        assertEquals(0.0, viewModel.total.value, 0.0)
    }
}
