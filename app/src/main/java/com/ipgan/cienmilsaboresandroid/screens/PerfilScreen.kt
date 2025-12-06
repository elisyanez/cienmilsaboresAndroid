package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipgan.cienmilsaboresandroid.R
import com.ipgan.cienmilsaboresandroid.model.User
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme
import com.ipgan.cienmilsaboresandroid.viewModel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    // 1. RECIBIMOS EL VIEWMODEL Y LOS DATOS DEL USUARIO
    userViewModel: UserViewModel = viewModel(),
    user: User, // Recibimos el usuario que ya está logueado
    onSave: () -> Unit,
    onBackToHome: () -> Unit
) {
    // 2. INICIALIZAMOS LOS CAMPOS CON LOS DATOS REALES DEL USUARIO
    // Usamos 'LaunchedEffect' para asegurarnos de que los campos se llenen
    // solo una vez cuando el composable aparece.
    var nombres by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var region by rememberSaveable { mutableStateOf("") }
    var comuna by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var nuevaPassword by rememberSaveable { mutableStateOf("") }
    var showPass by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        nombres = user.name.split(" ").firstOrNull() ?: ""
        apellidos = user.name.split(" ").drop(1).joinToString(" ")
        correo = user.email
        // Aquí puedes agregar la lógica para extraer región, comuna y dirección si estuvieran en un solo campo.
        // Por ahora, lo dejo como podrías manejarlo si vinieran separados.
        direccion = user.address ?: ""
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // --- Listas para los desplegables (esto se mantiene igual) ---
    val regiones = listOf("Región Metropolitana", "Valparaíso", "Biobío")
    val comunasRM = listOf("Santiago", "Providencia", "Las Condes")
    val comunasV  = listOf("Valparaíso", "Viña del Mar", "Quilpué")
    val comunasB  = listOf("Concepción", "Talcahuano", "Chiguayante")

    val comunasDisponibles = when (region) {
        "Región Metropolitana" -> comunasRM
        "Valparaíso" -> comunasV
        "Biobío" -> comunasB
        else -> emptyList()
    }

    var regionExpanded by remember { mutableStateOf(false) }
    var comunaExpanded by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- La UI se mantiene igual, solo cambia el botón de guardar ---
                    Image(
                        painter = painterResource(id = R.drawable.logo_mil_sabores),
                        contentDescription = "Mil Sabores",
                        modifier = Modifier.size(88.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(text = "Mi Perfil", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "Actualiza tus datos personales",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value = nombres,
                        onValueChange = { nombres = it },
                        label = { Text("Nombres") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = apellidos,
                        onValueChange = { apellidos = it },
                        label = { Text("Apellidos") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true, // El correo y el RUN no deberían ser editables.
                        enabled = false
                    )

                    // ... (El resto de los campos: región, comuna, dirección, contraseña)
                    // ... El código para los desplegables y otros TextFields va aquí...
                    // (Omitido por brevedad, es el mismo que ya tenías)
                    Spacer(Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = regionExpanded,
                        onExpandedChange = { regionExpanded = !regionExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = if (region.isBlank()) "Seleccione región" else region,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Región") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = regionExpanded,
                            onDismissRequest = { regionExpanded = false }
                        ) {
                            regiones.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        region = it
                                        comuna = ""
                                        regionExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = comunaExpanded,
                        onExpandedChange = {
                            if (region.isNotBlank()) comunaExpanded = !comunaExpanded
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = if (comuna.isBlank()) "Seleccione comuna" else comuna,
                            onValueChange = {},
                            readOnly = true,
                            enabled = region.isNotBlank(),
                            label = { Text("Comuna") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunaExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = comunaExpanded,
                            onDismissRequest = { comunaExpanded = false }
                        ) {
                            comunasDisponibles.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        comuna = it
                                        comunaExpanded = false
                                    }

                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nuevaPassword,
                        onValueChange = { nuevaPassword = it },
                        label = { Text("Nueva contraseña (opcional)") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        trailingIcon = {
                            TextButton(onClick = { showPass = !showPass }) {
                                Text(if (showPass) "Ocultar" else "Mostrar")
                            }
                        },
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )


                    Spacer(Modifier.height(24.dp))

                    // 3. IMPLEMENTAMOS LA LÓGICA DEL BOTÓN
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                // Creamos un nuevo objeto User con los datos actualizados del formulario
                                val updatedUser = user.copy(
                                    name = "$nombres $apellidos".trim(),
                                    email = correo, // El email no cambia
                                    address = "$direccion, $comuna, $region".trim(),
                                    // Si hay una nueva contraseña, la usamos, si no, mantenemos la original.
                                    password = if (nuevaPassword.isNotBlank()) nuevaPassword else user.password
                                )

                                // Llamamos a la función suspend del ViewModel
                                userViewModel.updateUser(updatedUser)

                                isLoading = false
                                snackbarHostState.showSnackbar("Perfil actualizado correctamente")
                                onSave() // Navegamos hacia atrás
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        } else {
                            Text("Guardar cambios")
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))

                    TextButton(
                        onClick = onBackToHome,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}

// 4. ACTUALIZAMOS LA PREVIEW PARA QUE FUNCIONE
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PerfilScreenPreview() {
    CienMilSaboresAndroidTheme {
        // Para la preview, creamos un usuario falso.
        val previewUser = User(run = "1-9", name = "Juan Pérez", email = "juan.perez@email.com", password = "123", address = "Av. Siempre Viva 123")
        PerfilScreen(user = previewUser, onSave = {}, onBackToHome = {})
    }
}
