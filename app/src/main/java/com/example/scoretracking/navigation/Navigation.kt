package com.example.scoretracking.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.scoretracking.model.Country
import com.example.scoretracking.screen.SportTrackerSplashScreen
import com.example.scoretracking.screen.favoritesScreen.FavoriteScreenViewModel
import com.example.scoretracking.screen.favoritesScreen.FavoritesSelection
import com.example.scoretracking.screen.splash.SplashScreenViewModel

@Composable
fun SportTrackerNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = SportTrackerScreens.SplashScreen.name) {
        composable(SportTrackerScreens.SplashScreen.name) { navBack ->
            navBack.let {
                val splashScreenViewModel  = hiltViewModel<SplashScreenViewModel>()
                SportTrackerSplashScreen(navController, splashScreenViewModel)
            }
        }
        composable(SportTrackerScreens.SelectFavoritesScreen.name) { navBack ->
            val favoriteScreenViewModel  = hiltViewModel<FavoriteScreenViewModel>()
            FavoritesSelection(navController, favoriteScreenViewModel)
        }
    }
}