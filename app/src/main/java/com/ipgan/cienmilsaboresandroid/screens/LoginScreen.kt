package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipgan.cienmilsaboresandroid.R
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme
import com.ipgan.cienmilsaboresandroid.viewModel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    userViewModel: UserViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onGoRegister: () -> Unit,
    onForgotPassword: (() -> Unit)? = null
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val user by userViewModel.user.collectAsState()

    // --- ESTA ES AHORA LA ÚNICA FUENTE DE VERDAD PARA NAVEGAR ---
    // Se ejecuta cuando el Composable entra en la pantalla y cada vez que 'user' cambia.
    LaunchedEffect(user) {
        if (user != null) {
            snackbarHostState.showSnackbar("¡Bienvenido, ${user!!.name}!")
            // La navegación ocurre como una reacción al cambio de estado.
            onLoginSuccess()
        }
    }

    fun validate(): Boolean {
        isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        isPasswordValid = password.length >= 6
        return isEmailValid && isPasswordValid
    }

    // Función unificada para manejar el intento de login
    fun attemptLogin() {
        if (validate()) {
            focusManager.clearFocus()
            scope.launch {
                isLoading = true
                val loginSuccess = userViewModel.login(email, password)
                isLoading = false

                // Si el login falla, el LaunchedEffect no se disparará.
                // Por lo tanto, mostramos el error aquí.
                if (!loginSuccess) {
                    snackbarHostState.showSnackbar("Email o contraseña incorrectos")
                }
            }
        }
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
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_mil_sabores),
                        contentDescription = "Mil Sabores",
                        modifier = Modifier.size(88.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Bienvenido a Pastelería Mil Sabores",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (!isEmailValid) isEmailValid = true
                        },
                        // ... (resto de las propiedades del OutlinedTextField)
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        isError = !isEmailValid,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) })
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (!isPasswordValid) isPasswordValid = true
                        },
                        // ... (resto de las propiedades)
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = !isPasswordValid,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { attemptLogin() }) // Llama a la función unificada
                    )

                    if (onForgotPassword != null) {
                        //... (botón de contraseña olvidada)
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = { attemptLogin() }, // Llama a la función unificada
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Entrar")
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))

                    TextButton(
                        onClick = onGoRegister,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        enabled = !isLoading
                    ) {
                        Text("Crear cuenta")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    CienMilSaboresAndroidTheme {
        LoginScreen(
            onLoginSuccess = {},
            onGoRegister = {},
            onForgotPassword = {}
        )
    }
}
