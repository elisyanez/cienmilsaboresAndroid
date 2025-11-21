package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipgan.cienmilsaboresandroid.data.ProductRepositoryFake
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme


@Composable
fun CarritoScreen(repo: ProductRepositoryFake) {
    // Se lee la propiedad `cart` directamente para que Compose pueda observar los cambios.
    val cart = repo.cart
    val total = cart.entries.sumOf { (product, quantity) -> product.precio_prod * quantity }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrito de compras")
        Spacer(modifier = Modifier.height(8.dp))


        if (cart.isEmpty()) {
            Text("El carrito está vacío.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(cart.entries.toList()) { (product, qty) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(product.nom_prod)
                            Text("Cantidad: $qty")
                            Text("Subtotal: $${product.precio_prod * qty}")
                        }
                        Button(onClick = { repo.removeFromCart(product) }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Text("Total: $total CLP", modifier = Modifier.padding(8.dp))


            Button (onClick = { repo.clearCart() }) {
                Text("Vaciar carrito")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarritoScreenPreview() {
    CienMilSaboresAndroidTheme {
        // Para la preview, creamos un repo y le añadimos productos
        // para simular un estado realista del carrito.
        val repo = remember {
            ProductRepositoryFake().apply {
                // Añadimos algunos productos para la vista previa
                getProducts().getOrNull(0)?.let { addToCart(it) }
                getProducts().getOrNull(1)?.let { product ->
                    addToCart(product)
                    addToCart(product) // Añadimos el segundo producto dos veces
                }
            }
        }
        CarritoScreen(repo = repo)
    }
}
