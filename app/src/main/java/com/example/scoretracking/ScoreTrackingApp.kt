package com.example.scoretracking

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.screen.SportTrackerSplashScreen
import com.example.scoretracking.screen.favoritesScreens.FavoritesSelection
import com.example.scoretracking.screen.favoritesScreens.FavoritesTeamsSelection
import com.example.scoretracking.ui.theme.ScoreTrackingTheme
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun ScoreTrackerApp() {
    ScoreTrackingTheme {

        val appState = rememberAppState()
        // A surface container using the 'background' color from the theme
        Scaffold(scaffoldState = appState.scaffoldState) { innerPaddingModifier ->
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
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, resources, coroutineScope) {
    ScoreTrackingAppState(scaffoldState, navController, resources, coroutineScope)
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

    composable(SportTrackerScreens.SelectFavoritesLeaguesScreen.name) {
        FavoritesSelection(openScreen = { route -> appState.navigate(route) })
    }

    composable(SportTrackerScreens.SelectFavoritesTeamsScreen.name) {
        FavoritesTeamsSelection(openScreen = { route -> appState.navigate(route) })
    }
//
//    composable(
//        route = "$EDIT_TASK_SCREEN$TASK_ID_ARG",
//        arguments = listOf(navArgument(TASK_ID) { defaultValue = TASK_DEFAULT_ID })
//    ) {
//        EditTaskScreen(
//            popUpScreen = { appState.popUp() },
//            taskId = it.arguments?.getString(TASK_ID) ?: TASK_DEFAULT_ID
//        )
//    }
}