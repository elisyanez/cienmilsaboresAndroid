package com.ipgan.cienmilsaboresandroid.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ipgan.cienmilsaboresandroid.R
import com.ipgan.cienmilsaboresandroid.model.Product
import com.ipgan.cienmilsaboresandroid.viewModel.ProductViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

val CATEGORIAS = listOf(
    "Tortas Cuadradas",
    "Tortas Circulares",
    "Postres Individuales",
    "Productos Sin Azúcar",
    "Pastelería Tradicional",
    "Productos Sin Gluten",
    "Productos Veganos",
    "Tortas Especiales"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
    productId: String? // Nullable: si es null, es para crear; si no, para editar
) {
    val context = LocalContext.current
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

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUri = uri
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                cameraLauncher.launch(uri)
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar("Permiso de cámara denegado.")
                }
            }
        }
    )

    LaunchedEffect(productId, productToEdit) {
        if (!isCreating && productId != null) {
            if (productToEdit == null || productToEdit?.id != productId) {
                productViewModel.loadProductById(productId)
            } else {
                productToEdit?.let {
                    id = it.id!!
                    name = it.name ?: ""
                    description = it.description ?: ""
                    category = it.category ?: ""
                    imageUrl = it.imageUrl ?: ""
                    price = it.price?.toString() ?: ""
                    stock = it.stock?.toString() ?: ""
                    isVisible = it.isVisible ?: true
                    imageUri = null // Reset local uri state
                }
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

            // --- SECCIÓN DE IMAGEN ---
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri ?: imageUrl.ifEmpty { R.drawable.logo_mil_sabores })
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.logo_mil_sabores),
                contentDescription = "Imagen del Producto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar Foto")
            }
            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            if (isCreating) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = id,
                        onValueChange = { id = it },
                        label = { Text("ID único (código)") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (category.isNotBlank()) {
                                val prefix = category.take(3).uppercase()
                                val randomNumber = (1000..9999).random()
                                id = "$prefix$randomNumber"
                            }
                        },
                        enabled = category.isNotBlank()
                    ) {
                        Text("Generar")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    CATEGORIAS.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                category = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), maxLines = 4)
            Spacer(modifier = Modifier.height(8.dp))

            // El campo de URL de imagen ya no es necesario, lo maneja la cámara
            // OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("URL de la Imagen") }, modifier = Modifier.fillMaxWidth())
            // Spacer(modifier = Modifier.height(8.dp))

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

                        // TODO: Implementar la subida de la imagen a un servidor.
                        // Por ahora, si hay una nueva foto (imageUri != null), se pasa como un string.
                        // El backend debe ser capaz de manejar esta URL de contenido o se debe
                        // subir la imagen a un servicio (ej. Firebase Storage) y obtener una URL HTTP.
                        val finalImageUrl = imageUri?.toString() ?: imageUrl

                        val product = Product(
                            id = if(isCreating) id else productToEdit!!.id,
                            name = name,
                            description = description,
                            category = category,
                            imageUrl = finalImageUrl,
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


fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}
