package com.example.scoretracking.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.screen.splash.SplashScreenViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun SportTrackerSplashScreen (
    navController: NavController,
    splashScreenViewModel: SplashScreenViewModel) {

    val leagueList = splashScreenViewModel.listLeague.collectAsState().value

    LaunchedEffect(key1 = leagueList.isNotEmpty()) {
//            if (leagueList.isNotEmpty()) {
                navController.navigate(SportTrackerScreens.SelectFavoritesScreen.name) {
                    popUpTo(SportTrackerScreens.SplashScreen.name) {
                        inclusive = true
                    }
//                }
            }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        contentAlignment = Alignment.Center) {
            Text(text = "LOADIIIIIING")
    }

}