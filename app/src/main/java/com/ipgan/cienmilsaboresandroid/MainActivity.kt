package com.ipgan.cienmilsaboresandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipgan.cienmilsaboresandroid.navigation.CienMilSaboresNavigation
import com.ipgan.cienmilsaboresandroid.ui.theme.*
import com.ipgan.cienmilsaboresandroid.viewModel.CarritoViewModel
import com.ipgan.cienmilsaboresandroid.viewModel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CienMilSaboresAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val usuarioViewModel: UsuarioViewModel = viewModel()
                    val carritoViewModel: CarritoViewModel = viewModel()

                    CienMilSaboresNavigation()
                }
            }
        }
    }
}
