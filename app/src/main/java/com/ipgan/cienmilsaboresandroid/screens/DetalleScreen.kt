package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ipgan.cienmilsaboresandroid.viewModel.ProductViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetalleScreen(
    navController: NavController,
    // ¡CAMBIO! El ID del producto ahora es un String.
    productId: String,
    productViewModel: ProductViewModel = viewModel()
) {
    val product by productViewModel.selectedProduct.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()

    LaunchedEffect(productId) {
        if (productId.isNotBlank()) {
            // ¡CAMBIO! Pasamos el ID como String al ViewModel.
            productViewModel.loadProductById(productId)
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            product?.let { p ->
                // Formateador para la moneda CLP
                val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                format.maximumFractionDigits = 0 // CLP no usa decimales

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = p.name ?: "Producto no encontrado",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = p.description ?: "Sin descripción",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        // ¡CAMBIO! Usamos el formateador de moneda para el precio Double.
                        text = format.format(p.price ?: 0.0),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text(text = "Volver al Catálogo")
                    }
                }
            } ?: run {
                // Esto se muestra si el producto no se encuentra o hay un error
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Producto no encontrado",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text(text = "Volver al Catálogo")
                    }
                }
            }
        }
    }
}
