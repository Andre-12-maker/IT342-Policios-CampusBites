package com.example.campusbites.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.campusbites.features.auth.ui.screen.LoginScreen
import com.example.campusbites.features.auth.ui.screen.RegisterScreen
import com.example.campusbites.features.cart.ui.screen.CartScreen
import com.example.campusbites.features.home.ui.screen.HomeScreen
import com.example.campusbites.features.order.ui.screen.MyOrdersScreen
import com.example.campusbites.features.order.ui.screen.PlaceOrderScreen

object Routes {
    const val HOME       = "home"
    const val CART       = "cart"
    const val LOGIN      = "login"
    const val REGISTER   = "register"
    const val PLACE_ORDER = "place_order"
    const val MY_ORDERS  = "my_orders"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = Routes.HOME,
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onCartClick   = { navController.navigate(Routes.CART) },
                onSignInClick = { navController.navigate(Routes.LOGIN) },
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess       = {
                    navController.navigate(Routes.MY_ORDERS) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
            )
        }
        composable(Routes.CART) {
            CartScreen(
                onCheckout    = { navController.navigate(Routes.PLACE_ORDER) },
                onSignInClick = { navController.navigate(Routes.LOGIN) },
            )
        }
        composable(Routes.PLACE_ORDER) {
            PlaceOrderScreen(
                onOrderSuccess = {
                    navController.navigate(Routes.MY_ORDERS) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                onSignInClick = { navController.navigate(Routes.LOGIN) },
            )
        }
        composable(Routes.MY_ORDERS) {
            MyOrdersScreen(
                onSignInClick = { navController.navigate(Routes.LOGIN) },
            )
        }
    }
}