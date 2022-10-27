package com.example.scoretracking.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scoretracking.commons.displayText
import com.example.scoretracking.commons.getWeekPageTitle
import com.example.scoretracking.commons.rememberFirstVisibleWeekAfterScroll
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.scoretracking.R.color as AppColor

@Composable
fun GamesScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val state = rememberWeekCalendarState(
            startDate = startDate,
            endDate = endDate,
            firstVisibleWeekDate = currentDate,
        )
        val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)
        TopAppBar(
            elevation = 0.dp,
            title = { Text(text = getWeekPageTitle(visibleWeek)) },
            /*navigationIcon = { NavigationIcon(onBackClick = close) },*/
        )
        WeekCalendar(
            modifier = Modifier.background(color = Color.LightGray.copy(alpha = 0.2f)),
            state = state,
            dayContent = { day ->
                Day(day.date, isSelected = selection == day.date) { clicked ->
                    if (selection != clicked) {
                        selection = clicked
                    }
                }
            },
        )

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
                .background(if (isSelected) Color.White else Color.Transparent ,
                            shape = CircleShape),
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
                    .background(colorResource(AppColor.example_7_yellow))
                    .align(Alignment.BottomCenter)
            )
        }
    }
}