package com.example.scoretracking

import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scoretracking.commons.snackbar.SnackBarManager
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.screen.SportTrackerSplashScreen
import com.example.scoretracking.screen.favorites.FavoritesSelection
import com.example.scoretracking.screen.favorites.FavoritesTeamsSelection
import com.example.scoretracking.screen.login.LoginScreen
import com.example.scoretracking.screen.login.RegisterScreen
import com.example.scoretracking.screen.main.GamesScreen
import com.example.scoretracking.screen.settings.SettingsScreen
import com.example.scoretracking.ui.theme.ScoreTrackingTheme
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun ScoreTrackerApp() {
    ScoreTrackingTheme {

        val appState = rememberAppState()
        // A surface container using the 'background' color from the theme
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier.padding(8.dp),
                    snackbar = { snackbarData ->
                        Snackbar(snackbarData, contentColor = MaterialTheme.colors.onPrimary)
                    }
                )
            },
            scaffoldState = appState.scaffoldState
        ) { innerPaddingModifier ->
            NavHost(
                navController = appState.navController,
                startDestination = SportTrackerScreens.SplashScreen.name,
                modifier = Modifier.padding(innerPaddingModifier)) { makeItSoGraph(appState) }

        }
    }
}


@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackBarManager = SnackBarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, resources, coroutineScope) {
    ScoreTrackingAppState(
        scaffoldState,
        navController,
        snackbarManager,
        resources,
        coroutineScope
    )
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}


@ExperimentalMaterialApi
fun NavGraphBuilder.makeItSoGraph(appState: ScoreTrackingAppState) {
    composable(SportTrackerScreens.SplashScreen.name) {
        SportTrackerSplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable("${SportTrackerScreens.SelectFavoritesLeaguesScreen.name}/{from}") { backEntry ->
        FavoritesSelection(openScreen = { route -> appState.navigate(route) }, backEntry.arguments?.getString("from"))
    }

    composable(SportTrackerScreens.SelectFavoritesTeamsScreen.name) {
        FavoritesTeamsSelection(openScreen = { route -> appState.navigate(route) })
    }

    composable(SportTrackerScreens.LoginScreen.name) {
        LoginScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

    composable(SportTrackerScreens.RegisterScreen.name) {
        RegisterScreen( openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SportTrackerScreens.GamesScreen.name) {
        GamesScreen(openScreen = { route -> appState.navigate(route) })
    }
    composable(SportTrackerScreens.SettingsScreen.name) {
        SettingsScreen(
            openScreen = { route -> appState.navigate(route)},
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }
}