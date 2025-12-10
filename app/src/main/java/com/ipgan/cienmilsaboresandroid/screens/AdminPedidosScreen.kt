package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ipgan.cienmilsaboresandroid.viewModel.PedidoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPedidosScreen(
    navController: NavController,
    pedidoViewModel: PedidoViewModel
) {
    val todosLosPedidos by pedidoViewModel.todosLosPedidos.collectAsState()
    val isLoading by pedidoViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        pedidoViewModel.cargarTodosLosPedidos()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Administrar Pedidos") }) }
    ) { innerPadding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (todosLosPedidos.isEmpty()) {
                    item {
                        Text(
                            "No se han encontrado pedidos.",
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(todosLosPedidos) { pedido ->
                        // Reutilizamos el PedidoCard, pero podríamos crear uno específico para admin si es necesario
                        Column {
                            Text("Usuario: ${pedido.usuario.name} (${pedido.usuario.run})", style=MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            PedidoCard(pedido = pedido, onClick = {
                                // navController.navigate(Screen.PedidoDetalle.createRoute(pedido.id))
                            })
                        }

                    }
                }
            }
        }
    }
}