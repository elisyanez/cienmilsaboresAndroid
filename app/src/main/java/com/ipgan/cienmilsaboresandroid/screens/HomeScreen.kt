package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipgan.cienmilsaboresandroid.R
import com.ipgan.cienmilsaboresandroid.model.User
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme

@Composable
fun HomeScreen(
    user: User?,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onNgrokConfigClick: () -> Unit,
    // --- NUEVAS ACCIONES PARA EL ADMIN ---
    onUserManagementClick: () -> Unit,
    onProductManagementClick: () -> Unit
) {
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

            Text(
                text = if (user != null) "¡Hola, ${user.name}!" else "Bienvenido a Pastelería Mil Sabores",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
            if (user?.role == "admin") {
                Text(
                    text = "Rol: Administrador",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("¿Qué deseas hacer hoy?", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))

            // --- BOTONES DE ACCIÓN ---
            Button(onClick = onCatalogClick, modifier = Modifier.fillMaxWidth(0.8f)) { Text("Ver Catálogo") }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onCartClick, modifier = Modifier.fillMaxWidth(0.8f)) { Text("Ir al Carrito") }
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoggedIn) {
                Button(onClick = onProfileClick, modifier = Modifier.fillMaxWidth(0.8f)) { Text("Mi Perfil") }
                Spacer(modifier = Modifier.height(16.dp))

                // --- BOTONES DE ADMIN ---
                if (user?.role == "admin") {
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Text("Panel de Administrador", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onUserManagementClick, modifier = Modifier.fillMaxWidth(0.8f)) { Text("Gestión de Usuarios") }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onProductManagementClick, modifier = Modifier.fillMaxWidth(0.8f)) { Text("Gestión de Productos") }
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                }

                Button(onClick = onLogoutClick, modifier = Modifier.fillMaxWidth(0.8f)) { Text("Cerrar Sesión") }
            } else {
                Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth(0.8f)) { Text("Iniciar Sesión") }
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
        title = { Text("Mil Sabores", style = MaterialTheme.typography.titleLarge) },
        actions = {
            if (!isLoggedIn) {
                IconButton(onClick = onNgrokConfigClick) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Configuración de Ngrok")
                }
            }
            if (isLoggedIn) {
                IconButton(onClick = onProfileClick) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Abrir perfil")
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
            user = User("123", "Admin", "User", "admin@test.com", "", "", "admin", "RM", "STGO"),
            onProfileClick = {},
            onCartClick = {},
            onCatalogClick = {},
            onLoginClick = {},
            onLogoutClick = {},
            onNgrokConfigClick = {},
            onUserManagementClick = {},
            onProductManagementClick = {}
        )
    }
}
