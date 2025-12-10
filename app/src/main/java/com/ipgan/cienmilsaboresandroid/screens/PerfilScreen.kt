package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ipgan.cienmilsaboresandroid.R
import com.ipgan.cienmilsaboresandroid.model.User
import com.ipgan.cienmilsaboresandroid.viewModel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    userViewModel: UserViewModel,
    user: User,
    onSave: () -> Unit,
    onBackToHome: () -> Unit
) {
    var nombres by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var region by rememberSaveable { mutableStateOf("") } // Este estado guarda el NOMBRE de la región
    var comuna by rememberSaveable { mutableStateOf("") } // Este estado guarda el NOMBRE de la comuna
    var direccion by rememberSaveable { mutableStateOf("") }
    var nuevaPassword by rememberSaveable { mutableStateOf("") }
    var showPass by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNewPasswordValid by remember { mutableStateOf(true) }

    val regiones by userViewModel.regiones.collectAsState()
    val allComunas by userViewModel.comunas.collectAsState()

    // Este efecto es ahora más robusto.
    // Puede manejar si `user.region` contiene un CÓDIGO o un NOMBRE.
    LaunchedEffect(user, regiones, allComunas) {
        nombres = user.name.split(" ").firstOrNull() ?: ""
        apellidos = user.name.split(" ").drop(1).joinToString(" ")
        correo = user.email
        direccion = user.address ?: ""
        // Intenta buscar el nombre por código. Si falla, asume que ya tiene el nombre.
        region = regiones.find { it.codigo == user.region }?.nombre ?: user.region ?: ""
        comuna = allComunas.find { it.codigo == user.commune }?.nombre ?: user.commune ?: ""
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val comunasDisponibles by remember(region, allComunas) {
        derivedStateOf {
            if (region.isNotBlank()) {
                val selectedRegion = regiones.find { it.nombre == region }
                allComunas.filter { it.regionCodigo == selectedRegion?.codigo }
            } else {
                emptyList()
            }
        }
    }

    var regionExpanded by remember { mutableStateOf(false) }
    var comunaExpanded by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        Box(
            modifier = Modifier.fillMaxSize().padding(inner).padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().widthIn(max = 420.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(id = R.drawable.logo_mil_sabores), contentDescription = "Mil Sabores", modifier = Modifier.size(88.dp))
                    Spacer(Modifier.height(12.dp))
                    Text(text = "Mi Perfil", style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Actualiza tus datos personales", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(24.dp))
                    OutlinedTextField(value = nombres, onValueChange = { nombres = it }, label = { Text("Nombres") }, leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") }, leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = correo, onValueChange = { }, label = { Text("Correo") }, leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) }, singleLine = true, modifier = Modifier.fillMaxWidth(), readOnly = true, enabled = false)
                    Spacer(Modifier.height(12.dp))

                    ExposedDropdownMenuBox(expanded = regionExpanded, onExpandedChange = { regionExpanded = !regionExpanded }, modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(value = if (region.isBlank()) "Seleccione región" else region, onValueChange = {}, readOnly = true, label = { Text("Región") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                        ExposedDropdownMenu(expanded = regionExpanded, onDismissRequest = { regionExpanded = false }) {
                            regiones.forEach { regionDto ->
                                DropdownMenuItem(text = { Text(regionDto.nombre) }, onClick = {
                                    region = regionDto.nombre
                                    comuna = ""
                                    regionExpanded = false
                                })
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    ExposedDropdownMenuBox(expanded = comunaExpanded, onExpandedChange = { if (region.isNotBlank()) comunaExpanded = !comunaExpanded }, modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(value = if (comuna.isBlank()) "Seleccione comuna" else comuna, onValueChange = {}, readOnly = true, enabled = region.isNotBlank(), label = { Text("Comuna") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunaExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                        ExposedDropdownMenu(expanded = comunaExpanded, onDismissRequest = { comunaExpanded = false }) {
                            comunasDisponibles.forEach { comunaDto ->
                                DropdownMenuItem(text = { Text(comunaDto.nombre) }, onClick = {
                                    comuna = comunaDto.nombre
                                    comunaExpanded = false
                                })
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = nuevaPassword, onValueChange = { nuevaPassword = it; isNewPasswordValid = true }, label = { Text("Nueva contraseña (opcional)") }, leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) }, trailingIcon = { TextButton(onClick = { showPass = !showPass }) { Text(if (showPass) "Ocultar" else "Mostrar") } }, visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(), singleLine = true, modifier = Modifier.fillMaxWidth(), isError = !isNewPasswordValid, supportingText = { if (!isNewPasswordValid) { Text("Debe tener al menos 6 caracteres") } })
                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val isPasswordOk = !(nuevaPassword.isNotBlank() && nuevaPassword.length < 6)
                            isNewPasswordValid = isPasswordOk

                            if (isPasswordOk) {
                                scope.launch {
                                    isLoading = true

                                    // 1. Buscamos el CÓDIGO de la región/comuna a partir del nombre seleccionado
                                    val regionCode = regiones.find { it.nombre == region }?.codigo ?: ""
                                    val comunaCode = allComunas.find { it.nombre == comuna }?.codigo ?: ""

                                    // 2. Creamos el objeto User para el ViewModel, enviando los CÓDIGOS
                                    val updatedUser = user.copy(
                                        name = "$nombres $apellidos".trim(),
                                        email = correo,
                                        address = direccion,
                                        region = regionCode,      // <-- ENVIAMOS EL CÓDIGO
                                        commune = comunaCode,    // <-- ENVIAMOS EL CÓDIGO
                                        password = nuevaPassword
                                    )

                                    userViewModel.updateUser(updatedUser)

                                    isLoading = false
                                    snackbarHostState.showSnackbar("Perfil actualizado correctamente")
                                    onSave()
                                }
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
                    TextButton(onClick = onBackToHome, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Volver") }
                }
            }
        }
    }
}
