package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipgan.cienmilsaboresandroid.R
// 1. IMPORTAMOS EL MODELO USER
import com.ipgan.cienmilsaboresandroid.model.User
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme

/**
 * Pantalla principal. Ahora recibe el objeto User para personalizar la experiencia.
 */
@Composable
fun HomeScreen(
    // 2. CAMBIAMOS isLoggedIn: Boolean por user: User?
    user: User?,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onNgrokConfigClick: () -> Unit
) {
    // La condición ahora es si el objeto 'user' no es nulo.
    val isLoggedIn = user != null

    Scaffold(
        topBar = {
            HomeTopAppBar(
                onProfileClick = onProfileClick,
                isLoggedIn = isLoggedIn,
                onNgrokConfigClick = onNgrokConfigClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_mil_sabores),
                contentDescription = "Logo de Pastelería Mil Sabores",
                modifier = Modifier.height(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. MENSAJE DE BIENVENIDA PERSONALIZADO
            Text(
                // Si el usuario existe, lo saludamos por su nombre. Si no, mostramos el texto genérico.
                text = if (user != null) "¡Hola, ${user.name}!" else "Bienvenido a Pastelería Mil Sabores",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "En nuestra pastelería online encontrarás una amplia variedad de productos de repostería elaborados con ingredientes frescos y de la más alta calidad. Desde tortas clásicas y pasteles personalizados hasta galletas y cupcakes, todo pensado para endulzar tus momentos más especiales.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Personaliza tu pedido",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Elige el diseño, el sabor y los detalles que prefieras para que tu pastel o postre sea único y perfecto para cualquier ocasión.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Compra fácil y rápida",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Explora nuestro catálogo, agrega tus productos favoritos al carrito y recibe tu pedido en la comodidad de tu hogar.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Cada bocado debe ser una experiencia única.",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "¿Qué deseas hacer hoy?",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCatalogClick,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Ver Catálogo")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCartClick,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Ir al Carrito")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoggedIn) {
                Button(
                    onClick = onProfileClick,
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Mi Perfil")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Cerrar Sesión")
                }
            } else {
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Iniciar Sesión")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    onProfileClick: () -> Unit,
    isLoggedIn: Boolean,
    onNgrokConfigClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Mil Sabores",
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            if (!isLoggedIn) {
                IconButton(onClick = onNgrokConfigClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuración de Ngrok"
                    )
                }
            }
            if (isLoggedIn) {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Abrir perfil"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    CienMilSaboresAndroidTheme {
        HomeScreen(
            user = null,
            onProfileClick = {},
            onCartClick = {},
            onCatalogClick = {},
            onLoginClick = {},
            onLogoutClick = {},
            onNgrokConfigClick = {}
        )

    }
}
