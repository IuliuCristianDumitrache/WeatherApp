package com.dumitrachecristian.weatherapp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.dumitrachecristian.weatherapp.MainViewModel

@Composable
fun NavGraph(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = MAIN_ROUTE,
            route = ROOT_ROUTE
        ) {
            mainNavGraph(navController = navController, viewModel = viewModel)
        }
    }
}