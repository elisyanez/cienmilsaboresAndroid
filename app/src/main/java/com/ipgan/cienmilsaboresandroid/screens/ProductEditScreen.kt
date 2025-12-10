package com.ipgan.cienmilsaboresandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.viewModel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
    productId: String? // Nullable: si es null, es para crear; si no, para editar
) {
    val isCreating = productId == null
    val productToEdit by productViewModel.selectedProduct.collectAsState()

    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Carga los datos si estamos en modo edición
    LaunchedEffect(productId, productToEdit) {
        if (!isCreating && productId != null) {
            if (productToEdit == null || productToEdit?.id != productId) {
                productViewModel.loadProductById(productId)
            } else {
                id = productToEdit!!.id!!
                name = productToEdit!!.name ?: ""
                description = productToEdit!!.description ?: ""
                category = productToEdit!!.category ?: ""
                imageUrl = productToEdit!!.imageUrl ?: ""
                price = productToEdit!!.price?.toString() ?: ""
                stock = productToEdit!!.stock?.toString() ?: ""
                isVisible = productToEdit!!.isVisible ?: true
            }
        } else {
             productViewModel.clearSelectedProduct()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text(if (isCreating) "Crear Producto" else "Editar Producto") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            if (isCreating) { // El ID solo se edita al crear
                 OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("ID único (código)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), maxLines = 4)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("URL de la Imagen") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Visible", modifier = Modifier.weight(1f))
                Switch(checked = isVisible, onCheckedChange = { isVisible = it })
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        val product = Product(
                            id = if(isCreating) id else productToEdit!!.id,
                            name = name,
                            description = description,
                            category = category,
                            imageUrl = imageUrl,
                            price = price.toDoubleOrNull() ?: 0.0,
                            stock = stock.toIntOrNull() ?: 0,
                            isVisible = isVisible
                        )

                        val success = if (isCreating) {
                            productViewModel.createProduct(product)
                        } else {
                            productViewModel.updateProduct(product)
                        }

                        if (success) {
                            snackbarHostState.showSnackbar("Producto guardado correctamente")
                            navController.popBackStack()
                        } else {
                            snackbarHostState.showSnackbar("Error al guardar el producto")
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar Producto")
                }
            }
        }
    }
}
