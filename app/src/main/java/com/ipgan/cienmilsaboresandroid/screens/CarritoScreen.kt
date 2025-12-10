package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.viewModel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CarritoScreen(carritoViewModel: CarritoViewModel = viewModel()) {
    // 1. CORRECCIÓN: Leemos el mapa de items directamente. Compose se encarga de observar los cambios.
    val items = carritoViewModel.itemsCarrito
    val total by carritoViewModel.total

    // Formateador para la moneda CLP
    val clpFormat = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 } }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrito de Compras", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("El carrito está vacío.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                // 2. Convertimos las entradas del mapa a una lista para poder mostrarla y ordenarla.
                items(items = items.entries.toList().sortedBy { it.key.name ?: "" }, key = { it.key.id!! }) { (product, qty) ->
                    CartItem(product = product, quantity = qty, clpFormat = clpFormat, onRemove = { carritoViewModel.eliminarItems(product) })
                    Divider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Total:", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(clpFormat.format(total), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { carritoViewModel.vaciarItems() }, modifier = Modifier.weight(1f)) {
                    Text("Vaciar Carrito")
                }
                Button(onClick = { /* TODO: Implementar lógica de pago */ }, modifier = Modifier.weight(1f)) {
                    Text("Proceder al Pago")
                }
            }
        }
    }
}

@Composable
fun CartItem(product: Product, quantity: Int, clpFormat: NumberFormat, onRemove: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(product.name ?: "Producto sin nombre", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Precio: ${clpFormat.format(product.price ?: 0.0)} c/u", style = MaterialTheme.typography.bodyMedium)
            Text("Cantidad: $quantity", style = MaterialTheme.typography.bodyMedium)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                clpFormat.format((product.price ?: 0.0) * quantity),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar del carrito", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
