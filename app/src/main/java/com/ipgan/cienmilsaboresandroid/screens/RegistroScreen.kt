package com.ipgan.cienmilsaboresandroid.screens

import android.annotation.SuppressLint
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
fun RegisterScreen(
    userViewModel: UserViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onGoLogin: () -> Unit,
    onBack: () -> Unit
) {
    var run by rememberSaveable { mutableStateOf("") }
    var nombres by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var region by rememberSaveable { mutableStateOf("") }
    var comuna by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val regiones by userViewModel.regiones.collectAsState()
    val allComunas by userViewModel.comunas.collectAsState()

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

    fun validate(): Boolean {
        val cleanRun = run.replace(".", "").replace("-", "")
        return cleanRun.length in 8..9 &&
                nombres.isNotBlank() &&
                apellidos.isNotBlank() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                region.isNotBlank() &&
                comuna.isNotBlank() &&
                direccion.isNotBlank() &&
                password.length >= 6
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { inner ->
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
                    Text(text = "Crear cuenta", style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Ingresa tus datos para registrarte", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(value = run, onValueChange = { run = it }, label = { Text("RUN (sin puntos y con guion)") }, leadingIcon = { Icon(Icons.Filled.AccountBox, contentDescription = null) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = nombres, onValueChange = { nombres = it }, label = { Text("Nombres") }, leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") }, leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo electrónico") }, leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) }, singleLine = true, modifier = Modifier.fillMaxWidth())
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
                    OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección (calle y número)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña (mínimo 6 caracteres)") }, leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) }, trailingIcon = { IconButton(onClick = { showPassword = !showPassword }) { Icon(imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null) } }, visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(), singleLine = true, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (validate()) {
                                scope.launch {
                                    isLoading = true

                                    // 1. Buscamos el CÓDIGO de la región/comuna a partir del nombre
                                    val regionCode = regiones.find { it.nombre == region }?.codigo ?: ""
                                    val comunaCode = allComunas.find { it.nombre == comuna }?.codigo ?: ""

                                    // 2. Creamos el usuario con los CÓDIGOS correctos
                                    val newUser = User(
                                        run = run,
                                        name = nombres,
                                        lastName = apellidos,
                                        email = email,
                                        address = direccion,
                                        region = regionCode,    // <-- ENVIAMOS EL CÓDIGO
                                        commune = comunaCode,  // <-- ENVIAMOS EL CÓDIGO
                                        password = password,
                                        role = "cliente"
                                    )

                                    val success = userViewModel.register(newUser)
                                    if (success) {
                                        snackbarHostState.showSnackbar("Registro exitoso")
                                        onRegisterSuccess()
                                    } else {
                                        snackbarHostState.showSnackbar("El correo o RUN ya está en uso")
                                    }
                                    isLoading = false
                                }
                            } else {
                                scope.launch { snackbarHostState.showSnackbar("Por favor, complete todos los campos correctamente.") }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Text("Registrarse")
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = onGoLogin, modifier = Modifier.align(Alignment.CenterHorizontally), enabled = !isLoading) { Text("¿Ya tienes cuenta? Inicia sesión") }
                    TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally), enabled = !isLoading) { Text("Volver") }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    CienMilSaboresAndroidTheme {
        RegisterScreen(
            onRegisterSuccess = {},
            onGoLogin = {},
            onBack = {}
        )
    }
}
