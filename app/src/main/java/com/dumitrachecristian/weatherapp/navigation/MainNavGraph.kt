package com.dumitrachecristian.weatherapp.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dumitrachecristian.weatherapp.ui.mainscreen.MainScreen
import com.dumitrachecristian.weatherapp.ui.mainscreen.viewmodel.MainViewModel
import com.dumitrachecristian.weatherapp.ui.mapscreen.MapsScreen
import com.dumitrachecristian.weatherapp.ui.settingsscreen.SettingsScreen

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
            SettingsScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = Screen.MapsScreen.route
        ) {
            MapsScreen(navController = navController, viewModel = viewModel)
        }
    }
}