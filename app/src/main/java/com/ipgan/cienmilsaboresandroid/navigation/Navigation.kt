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
import com.ipgan.cienmilsaboresandroid.viewModel.CarritoViewModel
import com.ipgan.cienmilsaboresandroid.viewModel.PedidoViewModel
import com.ipgan.cienmilsaboresandroid.viewModel.ProductViewModel
import com.ipgan.cienmilsaboresandroid.viewModel.UserViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Carrito : Screen("carrito")
    object Perfil : Screen("perfil")
    object Login : Screen("login")
    object Registro : Screen("registro")
    object Catalogo : Screen("catalogo")
    object Detalle : Screen("detalle/{productId}") {
        fun createRoute(productId: String) = "detalle/$productId"
    }
    object Splash : Screen("splash")
    object NgrokConfig : Screen("ngrok_config")
    object UserManagement : Screen("user_management")
    object ProductManagement : Screen("product_management")
    object ProductEdit : Screen("product_edit")

    // --- NUEVAS RUTAS DE PEDIDOS ---
    object ConfirmarPedido : Screen("confirmar_pedido")
    object MisPedidos : Screen("mis_pedidos")
    object AdminPedidos : Screen("admin_pedidos")
    object PedidoDetalle : Screen("pedido_detalle/{pedidoId}") {
        fun createRoute(pedidoId: Long) = "pedido_detalle/$pedidoId"
    }
}

@Composable
fun CienMilSaboresNavigation() {
    val navController = rememberNavController()

    // --- VIEWMODELS ---
    val userViewModel: UserViewModel = viewModel()
    val carritoViewModel: CarritoViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val pedidoViewModel: PedidoViewModel = viewModel()

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
                onNgrokConfigClick = { navController.navigate(Screen.NgrokConfig.route) },
                onUserManagementClick = { navController.navigate(Screen.UserManagement.route) },
                onProductManagementClick = { navController.navigate(Screen.ProductManagement.route) },
                onMisPedidosClick = { navController.navigate(Screen.MisPedidos.route) },
                onAdminPedidosClick = { navController.navigate(Screen.AdminPedidos.route) }
            )
        }

        composable(Screen.UserManagement.route) {
             UserManagementScreen(userViewModel = userViewModel)
        }

        composable(Screen.ProductManagement.route) {
             ProductManagementScreen(navController = navController, productViewModel = productViewModel)
        }

        composable(
            route = Screen.ProductEdit.route + "?productId={productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            ProductEditScreen(
                navController = navController,
                productViewModel = productViewModel,
                productId = backStackEntry.arguments?.getString("productId")
            )
        }

        composable(Screen.Perfil.route) {
            user?.let { loggedInUser ->
                PerfilScreen(
                    userViewModel = userViewModel,
                    user = loggedInUser,
                    onSave = { navController.popBackStack() },
                    onBackToHome = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Carrito.route) {
             CarritoScreen(
                navController = navController,
                carritoViewModel = carritoViewModel
            )
        }

        composable(Screen.Catalogo.route) {
            CatalogoScreen(navController = navController, carritoViewModel = carritoViewModel, productViewModel = productViewModel)
        }

        composable(Screen.Login.route) {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = { navController.navigate(Screen.Home.route) { popUpTo(navController.graph.startDestinationId) { inclusive = true } } },
                onGoRegister = { navController.navigate(Screen.Registro.route) },
                onForgotPassword = null
            )
        }

        composable(Screen.Registro.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Screen.Login.route) { popUpTo(Screen.Registro.route) { inclusive = true } } },
                onGoLogin = { navController.navigate(Screen.Login.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detalle.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            DetalleScreen(
                navController = navController,
                productId = productId,
                carritoViewModel = carritoViewModel,
                productViewModel = productViewModel
            )
        }

        composable(Screen.NgrokConfig.route) {
            NgrokConfigScreen()
        }

        // --- NAVEGACIÓN DE PEDIDOS ---
        composable(Screen.ConfirmarPedido.route) {
            ConfirmarPedidoScreen(
                navController = navController,
                userViewModel = userViewModel,
                carritoViewModel = carritoViewModel,
                pedidoViewModel = pedidoViewModel
            )
        }

        composable(Screen.MisPedidos.route) {
            user?.let { MisPedidosScreen(navController, pedidoViewModel, it.run) }
        }

        composable(Screen.AdminPedidos.route) {
             AdminPedidosScreen(navController, pedidoViewModel)
        }

        composable(
            route = Screen.PedidoDetalle.route,
            arguments = listOf(navArgument("pedidoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getLong("pedidoId")
            // Aquí necesitarás una pantalla de detalle de pedido
        }
    }
}
