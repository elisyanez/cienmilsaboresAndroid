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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ipgan.cienmilsaboresandroid.data.ProductRepositoryFake
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme

// Esto es para saber cuando está cargando.
private object Loading

@Composable
fun DetalleScreen(productId: Int, repo: ProductRepositoryFake, navController: NavController) {
    // Aquí obtenemos el producto del flow y usamos el centinela para el estado inicial.
    val productState by repo.getProductById(productId).collectAsState(initial = Loading)

    Scaffold {
        padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Mostramos el contenido según el estado actual (Cargando, Producto o nulo).
            when (val product = productState) {
                is Loading -> {
                    CircularProgressIndicator()
                }
                is Product -> {
                    ProductDetails(product = product, repo = repo, navController = navController)
                }
                null -> {
                    Text("Producto no encontrado")
                }
            }
        }
    }
}

@Composable
fun ProductDetails(product: Product, repo: ProductRepositoryFake, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = product.nom_prod, style = MaterialTheme.typography.headlineMedium)
        Text(text = product.desc_prod, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Precio: $${product.precio_prod}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Stock: ${product.stock_prod}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            repo.addToCart(product)
            // Volvemos para atrás después de agregar al carrito.
            navController.popBackStack() 
        }) {
            Text("Agregar al carrito")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetalleScreenPreview() {
    CienMilSaboresAndroidTheme {
        val repo = remember { ProductRepositoryFake() }
        val navController = rememberNavController()
        // Hacemos la preview con un producto que sabemos que existe.
        DetalleScreen(productId = 1, repo = repo, navController = navController)
    }
}
