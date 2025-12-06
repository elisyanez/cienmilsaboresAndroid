package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme
import com.ipgan.cienmilsaboresandroid.viewModel.CarritoViewModel
import com.ipgan.cienmilsaboresandroid.viewModel.ProductViewModel

@Composable
fun DetalleScreen(
    productId: Int,
    navController: NavController,
    // 1. AÑADIMOS LOS VIEWMODELS COMO PARÁMETROS.
    productViewModel: ProductViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel()
) {
    // 2. OBSERVAMOS EL PRODUCTO ESPECÍFICO DESDE EL PRODUCTVIEWMODEL.
    val product by productViewModel.selectedProduct.collectAsState()
    val isLoading by productViewModel.isLoading

    // 3. CARGAMOS EL PRODUCTO ESPECÍFICO CUANDO LA PANTALLA APARECE.
    // Usamos `LaunchedEffect` para llamar a la función `suspend` solo una vez.
    LaunchedEffect(productId) {
        productViewModel.loadProductById(productId)
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Mostramos el contenido según el estado (Cargando, Producto o nulo).
            if (isLoading) {
                CircularProgressIndicator()
            } else if (product != null) {
                // 4. PASAMOS EL CARRITOVIEWMODEL AL COMPOSABLE DE DETALLES.
                ProductDetails(
                    product = product!!, // Usamos '!!' porque ya comprobamos que no es nulo.
                    onAddToCart = {
                        carritoViewModel.agregarItems(product!!)
                        // Volvemos para atrás después de agregar al carrito.
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Producto no encontrado")
            }
        }
    }
}

@Composable
fun ProductDetails(product: Product, onAddToCart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = product.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.description, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Precio: ${product.price} CLP", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Stock disponible: ${product.stock}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onAddToCart) {
            Text("Agregar al carrito")
        }
    }
}

// Para la preview, ahora usamos los ViewModels como en las otras pantallas.
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetalleScreenPreview() {
    CienMilSaboresAndroidTheme {
        val navController = rememberNavController()
        DetalleScreen(productId = 1, navController = navController)
    }
}

