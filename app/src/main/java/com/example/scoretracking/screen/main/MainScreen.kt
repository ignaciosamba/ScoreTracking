package com.example.scoretracking.screen.main

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.screen.favorites.TopAppBarActionButton
import com.example.scoretracking.widgets.BottomNavMainApp
import java.time.LocalDate
import com.example.scoretracking.R.color as AppColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GamesScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel()
) {

    DisposableEffect(viewModel) {
        viewModel.addListener()
        onDispose { viewModel.removeListener() }
    }

    var bottomNavSelected : String by remember {
        mutableStateOf("Games")
    }

    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }

    Scaffold(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = { Text(text = bottomNavSelected) },
                backgroundColor = colorResource(id = AppColor.white),
                actions = {
                    // SEARCH button
                    TopAppBarActionButton(
                        imageVector = Icons.Default.Settings,
                        description = "Settings") {
                        openScreen(SportTrackerScreens.SettingsScreen.name)
                    }
                },
            )
        },
        bottomBar = {
            BottomNavMainApp(selected = bottomNavSelected) {
                bottomNavSelected = it
            }
        }) { innerPadding ->

        if(bottomNavSelected.equals("Games", ignoreCase = true)) {
            GameScreen(
                modifier = Modifier.padding(innerPadding),
                openScreen = openScreen,
                currentDate = currentDate,
                startDate = startDate,
                endDate = endDate
            )
        } else if (bottomNavSelected.equals("Standings", ignoreCase = true)) {
            StandingScreen(
                openScreen = openScreen,
                modifier = modifier.padding(innerPadding),
                viewModel = viewModel
            )
        }
    }
}
