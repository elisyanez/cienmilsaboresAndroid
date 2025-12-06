package com.ipgan.cienmilsaboresandroid.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class ProductViewModel: ViewModel() {
    val repository = ProductsRepository()

    private val _products = mutableStateOf<List<Product>?>(emptyList())
    val products: State<List<Product>?> = _products

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    suspend fun loadProducts() {
        _isLoading.value = true
        try {
            _products.value = repository.getProductos()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun createProduct(product: Product): Boolean {
        return try {
            repository.crearProducto(product)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateProduct(codigo: Int, product: Product): Boolean {
        return try {
            repository.updateProducto(codigo, product)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteProduct(codigo: Int): Boolean {
        return try {
            repository.eliminarProducto(codigo)
            true
        }
        catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Dentro de la clase ProductViewModel
    // ... (propiedades _products y _isLoading que ya tienes) ...
    // 1. AÑADE UN STATEFLOW PARA GUARDAR EL PRODUCTO SELECCIONADO.
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    // ... (función loadProducts() que ya tienes) ...
    // 2. AÑADE ESTA NUEVA FUNCIÓN PARA CARGAR UN PRODUCTO POR SU ID.
    suspend fun loadProductById(productId: Int) {
        _isLoading.value = true
        try {
            _selectedProduct.value = repository.getProductoByCodigo(productId)
        } catch (e: Exception) {
            e.printStackTrace()
            _selectedProduct.value = null // En caso de error, lo dejamos nulo.
        } finally {
            _isLoading.value = false
        }
    }

}