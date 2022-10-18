package com.example.scoretracking.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scoretracking.screen.SportTrackerSplashScreen
import com.example.scoretracking.screen.favoritesScreens.FavoriteLeaguesScreenViewModel
import com.example.scoretracking.screen.favoritesScreens.FavoritesSelection
import com.example.scoretracking.screen.favoritesScreens.FavoritesTeamsSelection
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
        composable(SportTrackerScreens.SelectFavoritesLeaguesScreen.name) { navBack ->
            val favoriteScreenViewModel  = hiltViewModel<FavoriteLeaguesScreenViewModel>()
            FavoritesSelection(navController, favoriteScreenViewModel)
        }
        composable(SportTrackerScreens.SelectFavoritesTeamsScreen.name) {
            val favoriteScreenViewModel  = hiltViewModel<FavoriteLeaguesScreenViewModel>()
            FavoritesTeamsSelection(navController/*, favoriteScreenViewModel*/)
        }
    }
}