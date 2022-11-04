package com.example.scoretracking.screen.main

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
import com.example.scoretracking.R
import com.example.scoretracking.commons.displayText
import com.example.scoretracking.commons.getWeekPageTitle
import com.example.scoretracking.commons.rememberFirstVisibleWeekAfterScroll
import com.example.scoretracking.model.thesportdbmodels.Event
import com.example.scoretracking.widgets.GameEventCard
import com.example.scoretracking.widgets.MotorSportEventCard
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel(),
    currentDate : LocalDate,
    startDate : LocalDate,
    endDate : LocalDate/*,
    selection : LocalDate,*/
) {
    val filteredEvent = viewModel.events

    var selection1 = viewModel.selectedDay

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.example_2_white_light))
    ) {
        val state = rememberWeekCalendarState(
            startDate = startDate,
            endDate = endDate,
            firstVisibleWeekDate = currentDate,
        )
        val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)
        TopAppBar(
            elevation = 2.dp,
            backgroundColor = colorResource(id = R.color.example_2_white_light),
            title = {
                Row(modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = getWeekPageTitle(visibleWeek),
                        modifier = Modifier.padding(start = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }

            },
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(color = colorResource(id = R.color.example_1_white_light))
        )
        Card(elevation = 2.dp) {
            WeekCalendar(
                modifier = Modifier.background(color = colorResource(id = R.color.example_2_white_light)),
                state = state,
                dayContent = { day ->
                    Day(day.date, isSelected = selection1.value == day.date) { clicked ->
                        if (selection1.value != clicked) {
                            filteredEvent.clear()
                            selection1.value = clicked
                            viewModel.getEventsByDate(selection1.value)
                        }
                    }
                },
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 5.dp)
                .fillMaxWidth()
        ) {
            if (filteredEvent.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    filteredEvent.forEach { (league, gameList) ->
                        stickyHeader {
                            StickyGameLeague(league)
                        }
                        items(items = gameList) { item ->
                            GameItem(item, viewModel) { eventId ->
                                Log.d("SAMBA", "CLICKED EVENT $eventId")
                            }
                        }
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}


private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
private fun Day(date: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit ) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(top = 10.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box (modifier = Modifier
                .aspectRatio(1f)
                .background(
                    if (isSelected) Color.White else Color.Transparent,
                    shape = CircleShape
                ),
                contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    Text(
                        text = date.dayOfWeek.displayText(),
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = dateFormatter.format(date),
                        fontSize = 17.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(colorResource(R.color.example_7_yellow))
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun StickyGameLeague(league : String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 5.dp)
        .background(color = colorResource(id = R.color.example_2_white_light))
        .height(25.dp)) {
        Text(
            text = league,
            modifier = Modifier.padding(horizontal = 17.dp, vertical = 2.dp),
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun GameItem(
    event : Event,
    viewModel : MainScreenViewModel,
    onClick: (String) ->  Unit) {
    if (event.strSport != "Motorsport" && event.strSport != "Fighting") {
        GameEventCard(event = event, viewModel = viewModel, onClick = onClick)
    } else {
        MotorSportEventCard(event = event, viewModel = viewModel, onClick = onClick)
    }
}