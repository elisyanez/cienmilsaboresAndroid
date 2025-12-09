package com.ipgan.cienmilsaboresandroid.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Heredamos de AndroidViewModel para tener el contexto de la aplicación
class ProductViewModel(application: Application) : AndroidViewModel(application) {

    // Usaremos StateFlow para un manejo de estado consistente y moderno.
    private val _products = MutableStateFlow<List<Product>?>(null)
    val products: StateFlow<List<Product>?> = _products.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // --- ESTA ES LA FUNCIÓN CLAVE CORREGIDA ---
    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. OBTENEMOS LA INSTANCIA DE LA API AQUÍ, CADA VEZ QUE LA NECESITAMOS.
                // Esto garantiza que siempre usamos la versión más reciente de la API,
                // ya sea la que no tiene token o la nueva que sí lo tiene.
                val api = RetrofitInstance2.getApi(getApplication())
                val response = api.getProductos()

                if (response.isSuccessful) {
                    _products.value = response.body()
                } else {
                    // Si la respuesta no es exitosa (ej: 404), asignamos una lista vacía.
                    _products.value = emptyList()
                }
            } catch (e: Exception) {
                // Si ocurre cualquier error (ej: sin conexión, API nula),
                // asignamos una lista vacía para evitar que la app crashee.
                e.printStackTrace()
                _products.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- CORREGIMOS TAMBIÉN LA CARGA POR ID ---
    fun loadProductById(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Hacemos lo mismo: obtener la API en el momento justo.
                val api = RetrofitInstance2.getApi(getApplication())
                val product = api.getProductoByCodigo(productId)
                _selectedProduct.value = product
            } catch (e: Exception) {
                e.printStackTrace()
                _selectedProduct.value = null // En caso de error, el producto es nulo.
            } finally {
                _isLoading.value = false
            }
        }
    }

}
