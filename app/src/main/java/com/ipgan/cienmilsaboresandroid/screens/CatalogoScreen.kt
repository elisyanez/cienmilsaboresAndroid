package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.navigation.Screen
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme
import com.ipgan.cienmilsaboresandroid.viewModel.CarritoViewModel
import com.ipgan.cienmilsaboresandroid.viewModel.ProductViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()

    // 1. Añadimos el estado para el Snackbar y el scope para lanzarlo
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
    }

    // 2. Usamos un Scaffold para poder mostrar el Snackbar
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Catálogo de Productos", fontWeight = FontWeight.Bold) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (products.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No hay productos disponibles.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = products, key = { product -> product.id!! }) { product ->
                        ProductItem(
                            product = product,
                            onClick = {
                                product.id?.let {
                                    navController.navigate(Screen.Detalle.createRoute(it))
                                }
                            },
                            // 3. Al añadir, mostramos el Snackbar
                            onAdd = {
                                carritoViewModel.agregarItems(product)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Producto agregado al carrito")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit, onAdd: () -> Unit) {
    val format = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 } }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name ?: "Producto sin nombre",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = format.format(product.price ?: 0.0),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatalogoScreenPreview() {
    CienMilSaboresAndroidTheme {
        val navController = rememberNavController()
        CatalogoScreen(navController = navController)
    }
}
