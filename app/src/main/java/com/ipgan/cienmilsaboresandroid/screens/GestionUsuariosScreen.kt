package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.ipgan.cienmilsaboresandroid.model.User
import com.ipgan.cienmilsaboresandroid.viewModel.UserViewModel

@Composable
fun GestionUsuariosScreen(viewModel: UserViewModel) {
    val navController = rememberNavController()


}


@Composable
fun UsuarioItem(usuario: User){
    Column(modifier = Modifier.fillMaxWidth()) {  }
}

@Composable
fun AgregarUsuario(onAddUsuario:(String,String)->Unit) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var run by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = {nombre = it},
            label = { Text("Ingrese Nombre") }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedTextField(
            value = apellido,
            onValueChange = {apellido = it},
            label = { Text("Ingrese Apellido") }
        )
        Button(onClick = {
            if (nombre.isNotBlank() && apellido.isNotBlank()) {
                onAddUsuario(nombre, apellido)
                nombre = ""
                apellido = ""
            }
        }
        ) {
            Text(text = "Agregar Usuario")
        }
    }
}