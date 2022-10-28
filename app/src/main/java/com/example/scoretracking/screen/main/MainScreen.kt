package com.example.scoretracking.screen.main

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scoretracking.commons.*
import com.example.scoretracking.model.Event
import com.example.scoretracking.widgets.BottomNavMainApp
import com.example.scoretracking.widgets.GameEventCard
import com.example.scoretracking.widgets.MotorSportEventCard
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.scoretracking.R.color as AppColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GamesScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameScreenViewModel = hiltViewModel()
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
