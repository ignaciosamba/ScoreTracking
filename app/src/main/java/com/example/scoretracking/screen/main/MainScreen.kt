package com.example.scoretracking.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
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
                backgroundColor = colorResource(id = AppColor.white)
            )
        },
        bottomBar = {
            BottomNavMainApp(selected = bottomNavSelected) {
                bottomNavSelected = it
            }
        }) { innerPadding ->
        if(bottomNavSelected.equals("Games", ignoreCase = true)) {
            viewModel.getEventsByDate()
            GameScreen(
                modifier = Modifier.padding(innerPadding),
                openScreen = openScreen,
                currentDate = currentDate,
                startDate = startDate,
                endDate = endDate
            )
        }
    }
}
