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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ipgan.cienmilsaboresandroid.model.Pedido
import com.ipgan.cienmilsaboresandroid.viewModel.PedidoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPedidosScreen(
    navController: NavController,
    pedidoViewModel: PedidoViewModel,
    usuarioRun: String
) {
    val misPedidos by pedidoViewModel.misPedidos.collectAsState()
    val isLoading by pedidoViewModel.isLoading.collectAsState()

    LaunchedEffect(usuarioRun) {
        pedidoViewModel.cargarMisPedidos(usuarioRun)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Pedidos") }) }
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
                if (misPedidos.isEmpty()) {
                    item {
                        Text(
                            "Aún no has realizado ningún pedido.",
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(misPedidos) { pedido ->
                        PedidoCard(pedido = pedido, onClick = {
                            // navController.navigate(Screen.PedidoDetalle.createRoute(pedido.id))
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun PedidoCard(pedido: Pedido, onClick: () -> Unit) {
    val clpFormat = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 } }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Pedido #${pedido.numeroPedido}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    pedido.estado.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(Modifier.height(8.dp))
            // Por simplicidad, parseamos la fecha. En un caso real, se usaría un parser más robusto.
            Text("Fecha: ${pedido.fecha.split("T").firstOrNull() ?: ""}", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total:", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(clpFormat.format(pedido.total), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }
        }
    }
}
