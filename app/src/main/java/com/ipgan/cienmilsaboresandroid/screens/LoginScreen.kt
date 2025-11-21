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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipgan.cienmilsaboresandroid.R
import com.ipgan.cienmilsaboresandroid.ui.theme.CienMilSaboresAndroidTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLogin: suspend (String, String) -> Unit,
    onGoRegister: () -> Unit,
    onForgotPassword: (() -> Unit)? = null
) {
    // Estos son nuestros estados para el formulario.
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    val focus = androidx.compose.ui.platform.LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun validate(): Boolean {
        isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        isPasswordValid = password.length >= 6
        return isEmailValid && isPasswordValid
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Esta es la tarjeta que diseñamos para el login.
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
                    // Aquí va nuestro logo.
                    Image(
                        painter = painterResource(id = R.drawable.logo_mil_sabores),
                        contentDescription = "Mil Sabores",
                        modifier = Modifier.size(88.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // El título y subtítulo de la pantalla.
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

                    // Campo para el correo.
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (!isEmailValid) isEmailValid = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        isError = !isEmailValid,
                        supportingText = {
                            if (!isEmailValid)
                                Text("El formato del correo no es válido", style = MaterialTheme.typography.bodySmall)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focus.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                        )
                    )

                    Spacer(Modifier.height(12.dp))

                    // Campo para la contraseña.
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (!isPasswordValid) isPasswordValid = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                if (showPassword)
                                    Icon(Icons.Filled.VisibilityOff, contentDescription = "Ocultar contraseña")
                                else
                                    Icon(Icons.Filled.Visibility, contentDescription = "Mostrar contraseña")
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = !isPasswordValid,
                        supportingText = {
                            if (!isPasswordValid)
                                Text("Mínimo 6 caracteres", style = MaterialTheme.typography.bodySmall)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                            autoCorrectEnabled = false
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (validate()) {
                                    focus.clearFocus()
                                    scope.launch {
                                        try {
                                            isLoading = true
                                            onLogin(email, password)
                                        } catch (e: Exception) {
                                            snackbarHostState.showSnackbar(e.message ?: "No se pudo iniciar sesión")
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            }
                        )
                    )

                    // Botón por si se olvida la contraseña.
                    if (onForgotPassword != null) {
                        TextButton(
                            onClick = onForgotPassword,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 4.dp)
                        ) {
                            Text("¿Olvidaste tu contraseña?")
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // El botón para entrar a la app.
                    Button(
                        onClick = {
                            if (validate()) {
                                scope.launch {
                                    try {
                                        isLoading = true
                                        onLogin(email, password)
                                    } catch (e: Exception) {
                                        snackbarHostState.showSnackbar(e.message ?: "No se pudo iniciar sesión")
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        },
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

                    // Un separador para que se vea más ordenado.
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))

                    // El botón para crear una cuenta nueva.
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

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    // Hay que envolver la preview con nuestro tema para que se vea como la app final.
    CienMilSaboresAndroidTheme {
        LoginScreen(
            onLogin = { _, _ -> },
            onGoRegister = {},
            onForgotPassword = {}
        )
    }
}
