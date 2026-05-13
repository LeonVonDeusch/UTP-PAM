package com.example.foodorderingapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.foodorderingapp.screen.MenuDetailScreen
import com.example.foodorderingapp.screen.MenuListScreen
import com.example.foodorderingapp.viewmodel.OrderViewModel

sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Int) = "detail/$itemId"
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: OrderViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Menu.route
    ) {
        composable(Screen.Menu.route) {
            MenuListScreen(
                viewModel = viewModel,
                onItemClick = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            MenuDetailScreen(
                viewModel = viewModel,
                itemId = itemId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
