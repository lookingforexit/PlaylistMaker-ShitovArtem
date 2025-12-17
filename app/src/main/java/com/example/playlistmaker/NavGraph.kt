package com.example.playlistmaker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playlistmaker.ui.screen.MainScreen
import com.example.playlistmaker.ui.screen.SearchScreen
import com.example.playlistmaker.ui.screen.SettingsScreen
import com.example.playlistmaker.ui.viewmodel.SearchViewModel

enum class ScreenRoute(val route: String) {
    Main("main"),
    Search("search"),
    Settings("settings")
}

class PlaylistHost(
    private val navController: NavHostController,
    private val viewModel: SearchViewModel
) {
    private fun navigateToSearch() {
        navController.navigate(ScreenRoute.Search.route)
    }

    private fun navigateToSettings() {
        navController.navigate(ScreenRoute.Settings.route)
    }

    private fun navigateBack() {
        navController.popBackStack()
    }
    @Composable
    fun NavGraph() {
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Main.route
        ) {
            composable(ScreenRoute.Main.route) {
                MainScreen(
                    onSearchClick = { navigateToSearch() },
                    onSettingsClick = { navigateToSettings() }
                )
            }
            composable(ScreenRoute.Search.route) {
                SearchScreen(onClick = { navigateBack() }, modifier = Modifier.fillMaxSize(), viewModel = viewModel)
            }
            composable(ScreenRoute.Settings.route) {
                SettingsScreen(onClick = { navigateBack() })
            }
        }
    }
}