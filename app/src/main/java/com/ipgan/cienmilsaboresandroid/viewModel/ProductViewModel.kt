package com.ipgan.cienmilsaboresandroid.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.repository.ProductsRepository // 1. IMPORTAMOS EL REPOSITORIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    // 2. CREAMOS UNA INSTANCIA DEL REPOSITORIO
    private val repository = ProductsRepository(application.applicationContext)

    // 3. INICIALIZAMOS EL ESTADO CON UNA LISTA VACÍA PARA MAYOR SEGURIDAD
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 4. ACTUALIZAMOS LA FUNCIÓN PARA USAR EL REPOSITORIO
    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Obtenemos los productos a través del repositorio
                _products.value = repository.getProductos() ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                _products.value = emptyList() // En caso de error, lista vacía
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 5. ACTUALIZAMOS LA CARGA POR ID PARA USAR STRING Y EL REPOSITORIO
    fun loadProductById(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Obtenemos el producto a través del repositorio
                _selectedProduct.value = repository.getProductoByCodigo(productId)
            } catch (e: Exception) {
                e.printStackTrace()
                _selectedProduct.value = null // En caso de error, producto nulo
            } finally {
                _isLoading.value = false
            }
        }
    }
}
