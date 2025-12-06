package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

// 1. AÑADIMOS LOS VIEWMODELS COMO PARÁMETROS
// Usamos la función `viewModel()` para que Compose nos entregue la instancia correcta.
@Composable
fun CatalogoScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel()
) {
    // 2. OBSERVAMOS EL ESTADO DEL VIEWMODEL DE PRODUCTOS
    // El `val products by ...` observa los cambios en la lista de productos.
    val products by productViewModel.products
    val isLoading by productViewModel.isLoading

    // 3. CARGAMOS LOS PRODUCTOS CUANDO LA PANTALLA APARECE POR PRIMERA VEZ
    // `LaunchedEffect` ejecuta la corrutina solo una vez.
    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Catálogo de Productos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 4. MANEJAMOS EL ESTADO DE CARGA (LOADING)
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Mostramos una ruedita de carga
            }
        } else if (products.isNullOrEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No hay productos disponibles.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre items
            ) {
                items(items = products!!, key = { it.id }) { product ->
                    ProductItem(
                        product = product,
                        onClick = { navController.navigate("detalle/${product.id}") },
                        // 5. USAMOS EL CARRITOVIEWMODEL PARA AÑADIR PRODUCTOS
                        onAdd = { carritoViewModel.agregarItems(product) }
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
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${product.price} CLP", // Agregamos "CLP" para claridad
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onAdd) {
                Text("Agregar")
            }
        }
    }
}

// La Preview sigue igual, ya que `viewModel()` también funciona en previews.
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatalogoScreenPreview() {
    CienMilSaboresAndroidTheme {
        val navController = rememberNavController()
        // No necesitamos pasar los ViewModels aquí, `viewModel()` lo hace por nosotros.
        CatalogoScreen(navController = navController)
    }
}
