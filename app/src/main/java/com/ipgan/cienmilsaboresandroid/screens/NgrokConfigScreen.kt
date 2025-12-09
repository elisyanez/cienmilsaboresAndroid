// ruta: screens/NgrokConfigScreen.kt

package com.ipgan.cienmilsaboresandroid.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipgan.cienmilsaboresandroid.config.NgrokManager
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme

@Composable
fun NgrokConfigScreen() {
    // Obtenemos el contexto de forma segura dentro del Composable
    val context = LocalContext.current

    // Estado para la URL actual que se muestra y edita
    var ngrokUrlInput by remember { mutableStateOf(NgrokManager.getBaseUrl(context)) }

    // Estado para la URL que está realmente en uso (solo para mostrar)
    val currentActiveUrl by rememberUpdatedState(NgrokManager.getBaseUrl(context))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Configuración de Ngrok",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Conecta la app con tu API local.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card para mostrar la URL en uso
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("URL en uso actualmente:", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = currentActiveUrl,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campo para editar la URL
        OutlinedTextField(
            value = ngrokUrlInput,
            onValueChange = { ngrokUrlInput = it },
            label = { Text("Nueva URL de Ngrok") },
            placeholder = { Text("https://xxxx-xxxx.ngrok-free.app") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Validación simple: la URL no puede estar vacía y debe ser una URL de ngrok
                if (ngrokUrlInput.isNotBlank() && "ngrok" in ngrokUrlInput) {
                    // Limpiamos la URL de espacios y del sufijo /api si el usuario lo puso.
                    val cleanUrl = ngrokUrlInput.trim().removeSuffix("/api").removeSuffix("/")

                    // Actualizamos la URL usando el Manager
                    NgrokManager.updateNgrokUrl(context, cleanUrl)

                    // Actualizamos el campo de texto para que muestre la URL formateada.
                    ngrokUrlInput = NgrokManager.getBaseUrl(context)

                    Toast.makeText(context, "URL actualizada. Reinicia la app.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "URL inválida. Pega la URL de Ngrok.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar y Guardar URL")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Instrucciones mejoradas
        Text("Instrucciones:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("1. En tu PC, donde corre tu API (ej: puerto 8080), ejecuta en la terminal:")
        Text("   ngrok http 8080 --region sa", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("2. Copia la URL 'Forwarding' que te da Ngrok (la que dice https).")
        Text("3. Pégala en el campo de arriba.")
        Text("4. Presiona 'Actualizar y Guardar URL'.")
        Text("5. ¡Importante! Cierra y vuelve a abrir la aplicación para que todos los servicios usen la nueva URL.")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NgrokConfigScreenPreview() {
    CienMilSaboresAndroidTheme {
        NgrokConfigScreen()
    }
}
