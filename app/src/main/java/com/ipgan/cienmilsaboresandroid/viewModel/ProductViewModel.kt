package com.ipgan.cienmilsaboresandroid.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductsRepository(application.applicationContext)

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _products.value = repository.getProductos() ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                _products.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadProductById(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedProduct.value = null // Limpiamos antes de cargar
            try {
                _selectedProduct.value = repository.getProductoByCodigo(productId)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedProduct() {
        _selectedProduct.value = null
    }

    suspend fun createProduct(product: Product): Boolean {
        val success = repository.crearProducto(product)
        if (success) {
            loadProducts()
        }
        return success
    }

    suspend fun updateProduct(product: Product): Boolean {
        if (product.id == null) return false
        val success = repository.updateProducto(product.id, product)
        if (success) {
            loadProducts()
            loadProductById(product.id)
        }
        return success
    }

    suspend fun deleteProduct(productId: String): Boolean {
        val success = repository.eliminarProducto(productId)
        if (success) {
            loadProducts()
        }
        return success
    }
}
