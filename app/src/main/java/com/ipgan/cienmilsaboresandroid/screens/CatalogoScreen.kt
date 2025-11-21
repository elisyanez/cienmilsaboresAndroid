package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@Composable
fun CatalogoScreen(repo: ProductRepositoryFake, navController: NavController) {
    // Se obtienen los productos directamente del repositorio.
    val products = repo.getProducts()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Catálogo de Productos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No hay productos disponibles.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = products, key = { it.id_prod }) { product ->
                    ProductItem(
                        product = product,
                        onClick = { navController.navigate("detalle/${product.id_prod}") },
                        onAdd = { repo.addToCart(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit, onAdd: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.nom_prod, style = MaterialTheme.typography.titleMedium)
                Text(text = "$${product.precio_prod}", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onAdd) {
                Text("Agregar")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatalogoScreenPreview() {
    CienMilSaboresAndroidTheme {
        // Para la preview, necesitamos crear instancias falsas de las dependencias.
        val navController = rememberNavController()
        val repo = remember { ProductRepositoryFake() }
        CatalogoScreen(repo = repo, navController = navController)
    }
}
