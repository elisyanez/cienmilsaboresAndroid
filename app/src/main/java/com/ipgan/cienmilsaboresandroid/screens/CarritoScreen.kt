package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.viewModel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(carritoViewModel: CarritoViewModel = viewModel()) {
    val items = carritoViewModel.itemsCarrito
    val total by carritoViewModel.total

    val clpFormat = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 } }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carrito de Compras", fontWeight = FontWeight.Bold) }) }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)) {
            if (items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("El carrito está vacío.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)) {
                    items(items = items.entries.toList().sortedBy { it.key.name ?: "" }, key = { it.key.id!! }) { (product, qty) ->
                        CartItem(
                            product = product,
                            quantity = qty,
                            clpFormat = clpFormat,
                            onAdd = { carritoViewModel.agregarItems(product) },
                            onSubtract = { carritoViewModel.restarItems(product) },
                            onRemove = { carritoViewModel.eliminarItems(product) }
                        )
                    }
                }

                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Total:", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(clpFormat.format(total), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { carritoViewModel.vaciarItems() }, modifier = Modifier.weight(1f)) { Text("Vaciar Carrito") }
                    Button(onClick = { /* TODO: Pagar */ }, modifier = Modifier.weight(1f)) { Text("Proceder al Pago") }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CartItem(
    product: Product,
    quantity: Int,
    clpFormat: NumberFormat,
    onAdd: () -> Unit,
    onSubtract: () -> Unit,
    onRemove: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Text(
                    text = product.name ?: "Producto sin nombre",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar producto", tint = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = onSubtract, modifier = Modifier.size(40.dp), contentPadding = PaddingValues(0.dp)) {
                        Icon(Icons.Default.Remove, contentDescription = "Restar uno")
                    }
                    Text("$quantity", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 16.dp))
                    OutlinedButton(onClick = onAdd, modifier = Modifier.size(40.dp), contentPadding = PaddingValues(0.dp)) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir uno")
                    }
                }

                Text(
                    text = clpFormat.format((product.price ?: 0.0) * quantity),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
