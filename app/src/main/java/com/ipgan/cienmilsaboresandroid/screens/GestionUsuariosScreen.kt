package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipgan.cienmilsaboresandroid.model.User
import com.ipgan.cienmilsaboresandroid.viewModel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(userViewModel: UserViewModel = viewModel()) {
    val users by userViewModel.allUsers.collectAsState()
    val currentUser by userViewModel.user.collectAsState()

    var userToDelete by remember { mutableStateOf<User?>(null) }
    var showFirstConfirmDialog by remember { mutableStateOf(false) }
    var showSecondConfirmDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        userViewModel.loadAllUsers()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Gestión de Usuarios") }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = users, key = { it.run }) {
                UserManagementItem(
                    user = it,
                    isCurrentUserAdmin = currentUser?.role == "admin",
                    onDeleteClick = {
                        userToDelete = it
                        showFirstConfirmDialog = true
                    }
                )
            }
        }
    }

    // --- Diálogos de Confirmación ---
    if (showFirstConfirmDialog && userToDelete != null) {
        AlertDialog(
            onDismissRequest = { showFirstConfirmDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Seguro que quieres eliminar al usuario ${userToDelete!!.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        showFirstConfirmDialog = false
                        showSecondConfirmDialog = true // Abrir el segundo diálogo
                    }
                ) { Text("Sí, eliminar") }
            },
            dismissButton = { TextButton(onClick = { showFirstConfirmDialog = false }) { Text("Cancelar") } }
        )
    }

    if (showSecondConfirmDialog && userToDelete != null) {
        AlertDialog(
            onDismissRequest = { showSecondConfirmDialog = false },
            title = { Text("¡Acción Permanente!") },
            text = { Text("Esta acción no se puede deshacer. Por favor, confirma de nuevo la eliminación de ${userToDelete!!.name}.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val success = userViewModel.deleteUser(userToDelete!!.run)
                            if (success) {
                                snackbarHostState.showSnackbar("Usuario eliminado correctamente")
                            } else {
                                snackbarHostState.showSnackbar("Error al eliminar el usuario")
                            }
                        }
                        showSecondConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Confirmar y Borrar") }
            },
            dismissButton = { TextButton(onClick = { showSecondConfirmDialog = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
fun UserManagementItem(user: User, isCurrentUserAdmin: Boolean, onDeleteClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, fontWeight = FontWeight.Bold)
                Text(text = user.email, style = MaterialTheme.typography.bodySmall)
                Text(text = "Rol: ${user.role}", style = MaterialTheme.typography.bodySmall, color = if(user.role == "admin") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface)
            }
            // El admin no se puede borrar a sí mismo ni a otros admins
            val canBeDeleted = user.role != "admin"
            IconButton(onClick = onDeleteClick, enabled = canBeDeleted) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Usuario",
                    tint = if (canBeDeleted) MaterialTheme.colorScheme.error else Color.Gray
                )
            }
        }
    }
}
