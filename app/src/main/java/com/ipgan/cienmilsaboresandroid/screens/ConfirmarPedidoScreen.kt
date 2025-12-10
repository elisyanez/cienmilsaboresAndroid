package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ipgan.cienmilsaboresandroid.navigation.Screen
import com.ipgan.cienmilsaboresandroid.viewModel.CarritoViewModel
import com.ipgan.cienmilsaboresandroid.viewModel.PedidoViewModel
import com.ipgan.cienmilsaboresandroid.viewModel.UserViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarPedidoScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    carritoViewModel: CarritoViewModel,
    pedidoViewModel: PedidoViewModel
) {
    val currentUser by userViewModel.user.collectAsState()
    val items by remember { mutableStateOf(carritoViewModel.itemsCarrito) }
    val total by carritoViewModel.total

    val isLoading by pedidoViewModel.isLoading.collectAsState()
    val pedidoCreado by pedidoViewModel.pedidoCreado.collectAsState()
    val error by pedidoViewModel.error.collectAsState()

    var showConfirmationDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val clpFormat = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 } }

    // Efecto para manejar la creación exitosa del pedido
    LaunchedEffect(pedidoCreado) {
        if (pedidoCreado != null) {
            scope.launch {
                snackbarHostState.showSnackbar("¡Pedido #${pedidoCreado?.numeroPedido} creado con éxito!")
            }
            carritoViewModel.vaciarItems()
            pedidoViewModel.limpiarPedidoCreado()
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        }
    }

    // Efecto para manejar los errores
    LaunchedEffect(error) {
        error?.let {
            scope.launch {
                snackbarHostState.showSnackbar("Error: $it")
            }
            pedidoViewModel.limpiarError() // Limpiamos el error después de mostrarlo
        }
    }

    // Diálogo de confirmación
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirmar Pedido") },
            text = { Text("¿Estás seguro de que quieres realizar este pedido?") },
            confirmButton = {
                Button(
                    onClick = {
                        currentUser?.run?.let { run ->
                            pedidoViewModel.crearPedido(run, items)
                        }
                        showConfirmationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmationDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Confirmar Pedido") }) }
    ) { innerPadding ->
        if (currentUser == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Error: Debes iniciar sesión para realizar un pedido.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                    Text("Creando pedido...", modifier = Modifier.padding(top = 70.dp))
                }
            } else {
                // Contenido de la pantalla
                LazyColumn(modifier = Modifier.weight(1f)) {
                    item { Text("Resumen del Pedido", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                    items(items.entries.toList()) { (product, qty) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${qty}x ${product.name}")
                            Text(clpFormat.format((product.price ?: 0.0) * qty))
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                    item { 
                        Divider(Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", fontWeight = FontWeight.Bold)
                            Text(clpFormat.format(total), fontWeight = FontWeight.Bold)
                        } 
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                    item { Text("Información de Envío", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                    item { 
                        Text("Dirección:", fontWeight = FontWeight.Bold)
                        Text(currentUser?.address ?: "No especificada")
                        Spacer(Modifier.height(8.dp))
                        Text("Comuna:", fontWeight = FontWeight.Bold)
                        Text(currentUser?.commune ?: "No especificada")
                        Spacer(Modifier.height(8.dp))
                        Text("Región:", fontWeight = FontWeight.Bold)
                        Text(currentUser?.region ?: "No especificada")
                    }
                }
                
                Button(
                    onClick = { showConfirmationDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = items.isNotEmpty() && !isLoading
                ) {
                    Text("Confirmar y Pagar")
                }
            }
        }
    }
}
