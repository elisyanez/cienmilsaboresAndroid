package com.ipgan.cienmilsaboresandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ipgan.cienmilsaboresandroid.screens.*
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
    object NgrokConfig : Screen("ngrok_config")
}

@Composable
fun CienMilSaboresNavigation() {
    val navController = rememberNavController()
    // Este ViewModel se comparte entre todas las pantallas del NavHost
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
                user = user,
                onProfileClick = { navController.navigate(Screen.Perfil.route) },
                onCartClick = { navController.navigate(Screen.Carrito.route) },
                onCatalogClick = { navController.navigate(Screen.Catalogo.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onLogoutClick = { userViewModel.logout() },
                onNgrokConfigClick = { navController.navigate(Screen.NgrokConfig.route) }
            )
        }

        composable(Screen.Perfil.route) {
            user?.let { loggedInUser ->
                PerfilScreen(
                    userViewModel = userViewModel,
                    user = loggedInUser,
                    onSave = {
                        navController.popBackStack()
                    },
                    onBackToHome = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Screen.Carrito.route) {
            CarritoScreen()
        }

        composable(Screen.Catalogo.route) {
            CatalogoScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            // AHORA SÍ LE PASAMOS EL VIEWMODEL COMPARTIDO
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                onGoRegister = { navController.navigate(Screen.Registro.route) },
                onForgotPassword = null
            )
        }

        composable(Screen.Registro.route) {
            RegisterScreen(
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
                navController = navController,
                productId = productId
            )
        }

        composable(Screen.NgrokConfig.route) {
            NgrokConfigScreen()
        }
    }
}
