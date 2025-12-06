package com.ipgan.cienmilsaboresandroid.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipgan.cienmilsaboresandroid.R
import com.ipgan.cienmilsaboresandroid.model.User
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme
import com.ipgan.cienmilsaboresandroid.viewModel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    userViewModel: UserViewModel,
    onRegisterSuccess: () -> Unit,
    onGoLogin: () -> Unit,
    onBack: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var run by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun validate(): Boolean {
        return run.length>=9 && run.contains('-') &&
                name.isNotBlank() &&
                email.isNotBlank() &&
                address.isNotBlank() &&
                password.length >= 6
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { inner ->
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
                    Image(
                        painter = painterResource(id = R.drawable.logo_mil_sabores),
                        contentDescription = "Mil Sabores",
                        modifier = Modifier.size(88.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Crear cuenta",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Ingresa tus datos para registrarte",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value = run,
                        onValueChange = { run = it },
                        label = { Text("RUN (sin puntos y con guion)") },
                        leadingIcon = { Icon(Icons.Filled.AccountBox, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Dirección") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña (mínimo 6 caracteres)") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (validate()) {
                                scope.launch {
                                    isLoading = true
                                    val newUser = User(run= run, name = name, email = email, address = address, password = password)
                                    val success = userViewModel.register(newUser)
                                    if (success) {
                                        snackbarHostState.showSnackbar("Registro exitoso")
                                        onRegisterSuccess()
                                    } else {
                                        snackbarHostState.showSnackbar("El correo electrónico ya está en uso")
                                    }
                                    isLoading = false
                                }
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Por favor, complete todos los campos.")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text("Registrarse")
                    }

                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))

                    TextButton(
                        onClick = onGoLogin,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("¿Ya tienes cuenta? Inicia sesión")
                    }

                    TextButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Volver")
                    }
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
            userViewModel = UserViewModel(),
            onRegisterSuccess = {},
            onGoLogin = {},
            onBack = {}
        )
    }
}
