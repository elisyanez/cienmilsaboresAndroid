package com.ipgan.cienmilsaboresandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ipgan.cienmilsaboresandroid.data.ProductRepositoryFake
import com.ipgan.cienmilsaboresandroid.screens.CarritoScreen
import com.ipgan.cienmilsaboresandroid.screens.CatalogoScreen
import com.ipgan.cienmilsaboresandroid.screens.DetalleScreen
import com.ipgan.cienmilsaboresandroid.screens.HomeScreen
import com.ipgan.cienmilsaboresandroid.screens.LoginScreen
import com.ipgan.cienmilsaboresandroid.screens.PerfilScreen
import com.ipgan.cienmilsaboresandroid.screens.RegisterScreen
import com.ipgan.cienmilsaboresandroid.screens.SplashScreen
import com.ipgan.cienmilsaboresandroid.viewModel.UserViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Carrito : Screen("carrito")
    object Perfil : Screen("perfil")
    object Login : Screen("login")
    object Registro : Screen("registro")
    object Catalogo : Screen("catalogo")
    object Detalle : Screen("detalle/{productId}") {
        fun createRoute(productId: Int) = "detalle/$productId"
    }
    object Splash : Screen("splash")
}

@Composable
fun CienMilSaboresNavigation() {
    val navController = rememberNavController()
    val repo = remember { ProductRepositoryFake() }
    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.user.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(
                isLoggedIn = user != null,
                onProfileClick = { navController.navigate(Screen.Perfil.route) },
                onCartClick = { navController.navigate(Screen.Carrito.route) },
                onCatalogClick = { navController.navigate(Screen.Catalogo.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onLogoutClick = { userViewModel.logout() }
            )
        }

        composable(Screen.Perfil.route) {
            user?.let { // Solo se puede acceder si el usuario está logueado
                PerfilScreen(
                    onSave = { 
                        // Lógica para guardar los datos del perfil, posiblemente en el ViewModel
                        navController.popBackStack()
                     },
                    onBackToHome = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Carrito.route) {
            CarritoScreen(repo = repo)
        }

        composable(Screen.Catalogo.route) {
            CatalogoScreen(repo = repo, navController = navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = { email, pass ->
                    if (!userViewModel.login(email, pass)) {
                        throw Exception("Email o contraseña incorrectos")
                    }
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } // Remove login from backstack
                    }
                },
                onGoRegister = { navController.navigate(Screen.Registro.route) },
                onForgotPassword = null // Explicitly passing null for the optional parameter
            )
        }

        composable(Screen.Registro.route) {
            RegisterScreen(
                userViewModel = userViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Registro.route) { inclusive = true }
                    }
                },
                onGoLogin = { navController.navigate(Screen.Login.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detalle.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            DetalleScreen(
                repo = repo,
                navController = navController,
                productId = productId
            )
        }
    }
}