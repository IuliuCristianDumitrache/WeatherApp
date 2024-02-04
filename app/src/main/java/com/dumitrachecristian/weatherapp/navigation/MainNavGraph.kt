package com.dumitrachecristian.weatherapp.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dumitrachecristian.weatherapp.MainScreen
import com.dumitrachecristian.weatherapp.MainViewModel
import com.dumitrachecristian.weatherapp.ui.ManageLocationsScreen
import com.dumitrachecristian.weatherapp.ui.SettingsScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel,
) {
    navigation(
        startDestination = Screen.MainScreen.route,
        route = MAIN_ROUTE,
    ) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = Screen.SettingsScreen.route
        ) {
            SettingsScreen(navController = navController)
        }

        composable(
            route = Screen.ManageLocationsScreen.route
        ) {
            ManageLocationsScreen(navController = navController)
        }
    }
}