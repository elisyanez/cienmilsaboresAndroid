package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // 1. IMPORTAMOS viewModel()
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme
import com.ipgan.cienmilsaboresandroid.viewModel.CarritoViewModel

@Composable
// 2. ELIMINAMOS EL PARÁMETRO DE LA FUNCIÓN
fun CarritoScreen(
    // El parámetro `repo: CarritoViewModel` se ha ido.
    // En su lugar, lo obtenemos aquí adentro:
    carritoViewModel: CarritoViewModel = viewModel()
) {
    // 3. USAMOS LA NUEVA VARIABLE `carritoViewModel`
    val items = carritoViewModel.itemsCarrito
    // OJO: El total ahora se observa directamente desde el ViewModel.
    // No es necesario usar `.value` si se declara como State<Double> y se usa 'by'.
    // Para que funcione como está, he dejado `carritoViewModel.total.value`.
    val total = carritoViewModel.total.value

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrito de compras")
        Spacer(modifier = Modifier.height(8.dp))

        if (items.isEmpty()) {
            Text("El carrito está vacío.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                // Usamos `items.toList()` porque `LazyColumn` prefiere listas.
                items(items.entries.toList()) { (product, qty) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            // Asumo que tu modelo Product tiene `name`, `price`, etc.
                            // Si los nombres de las propiedades son nom_prod, precio_prod, ajústalo aquí.
                            Text(text = product.name ?: "Producto sin nombre")
                            Text("Precio: ${product.price} CLP")
                            Text("Cantidad: $qty")
                            Text(
                                "Subtotal: ${(product.price ?: 0.0).toDouble()* qty} CLP" // <--- CAMBIO CLAVE
                            )
                        }
                        // 4. USAMOS LA NUEVA VARIABLE `carritoViewModel`
                        Button(onClick = { carritoViewModel.eliminarItems(product) }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Text("Total: $total CLP", modifier = Modifier.padding(8.dp))

            // 5. USAMOS LA NUEVA VARIABLE `carritoViewModel`
            Button(onClick = { carritoViewModel.vaciarItems() }) {
                Text("Vaciar carrito")
            }
        }
    }
}
