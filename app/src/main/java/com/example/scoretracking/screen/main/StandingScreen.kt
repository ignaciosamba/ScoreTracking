package com.example.scoretracking.screen.main

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scoretracking.model.firebasemodels.StorageLeague
import com.example.scoretracking.network.F1_LEAGUE_ID
import com.example.scoretracking.network.NBA_LEAGUE_ID
import com.example.scoretracking.widgets.TeamStandingTableItem

@Composable
fun StandingScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel : MainScreenViewModel = hiltViewModel()
) {

    val filteredEvent = viewModel.standings

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.favoriteLeaguesFromStorage.isNotEmpty()) {
            var leagues = ArrayList<StorageLeague>()
            viewModel.favoriteLeaguesFromStorage.forEach {
                leagues.add(it.value)
            }
            CompileLeagueList(leagues.toList(), viewModel)
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompileLeagueList(leagues : List<StorageLeague>,
                      viewModel: MainScreenViewModel
) {

    leagues.forEach { league ->
        // This is used for the teams lists.
        val isClicked = remember {
            mutableStateOf(false)
        }
        // This is used only for the icons.
        val isClickedForIcon = remember {
            mutableStateOf(false)
        }

        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 15.dp, top = 5.dp)
                .clickable {
                    isClicked.value = !isClicked.value
                    if (isClicked.value) {
                        viewModel.getStandingByLeague(league.idLeague, league.strCurrentSeason)
                    } else {
                        isClickedForIcon.value = false
                    }
                }) {
                Box(contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .height(40.dp)
                        .padding(start = 20.dp)) {
                    Text(
                        text = league.strLeague,
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 19.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Crossfade(targetState = isClickedForIcon.value,
                    animationSpec = tween(100)
                ) { isChecked ->
                    Icon(imageVector = if(isChecked) Icons.Default.KeyboardArrowUp
                                else Icons.Default.KeyboardArrowDown,
                        contentDescription = "arrow to open",
                        modifier = Modifier
                            .height(48.dp)
                            .width(48.dp)
                            .padding(end = 15.dp))
                }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            if (isClicked.value && !viewModel.standings[league.idLeague].isNullOrEmpty()) {
                isClickedForIcon.value = true
                if (league.idLeague.equals(NBA_LEAGUE_ID, ignoreCase = true)) {
                    val filteredList = viewModel.standings[league.idLeague]?.sortedBy { it.position.toInt() }?.groupBy { it.division }
                    LazyColumn(contentPadding = PaddingValues(4.dp)) {
                        filteredList?.forEach { (division, standingModelList) ->
                            stickyHeader {
                                TeamStandingTableItem(
                                    modifier = Modifier.background(Color.White),
                                    position = "#",
                                    teamName = division,
                                    teamBadge = "",
                                    teamDiff = "L",
                                    teamGamesPlayed = "W",
                                    teamPts = "PCT"
                                )
                            }
                            items(items = standingModelList) { item ->
                                TeamStandingTableItem(
                                    modifier = Modifier.background(Color.Transparent),
                                    position = item.position,
                                    teamName = item.name,
                                    teamBadge = item.icon,
                                    teamDiff = item.seconRow,
                                    teamGamesPlayed = item.firstRow,
                                    teamPts = item.thirdRow
                                )
                            }
                        }
                    }
                } else if (league.idLeague.equals(F1_LEAGUE_ID, ignoreCase = true)) {
                    val filteredList =
                        viewModel.standings[league.idLeague]?.sortedBy { it.position.toInt() }
                            ?.groupBy { it.division }
                    LazyColumn(contentPadding = PaddingValues(4.dp)) {
                        filteredList?.forEach { (division, standingModelList) ->
                            stickyHeader {
                                TeamStandingTableItem(
                                    modifier = Modifier.background(Color.White),
                                    position = "#",
                                    teamName = division,
                                    teamBadge = "",
                                    teamDiff = "",
                                    teamGamesPlayed = "",
                                    teamPts = "PTS"
                                )
                            }
                            items(items = standingModelList) { item ->
                                TeamStandingTableItem(
                                    modifier = Modifier.background(Color.Transparent),
                                    position = item.position,           // position
                                    teamName = item.name,               // Name
                                    teamBadge = item.icon,              // icono
                                    teamDiff = item.seconRow,           // segunda columna
                                    teamGamesPlayed = item.firstRow,    // primera columna
                                    teamPts = item.thirdRow             // tercera columna
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(contentPadding = PaddingValues(4.dp)) {
                        stickyHeader {
                            TeamStandingTableItem(
                                modifier = Modifier.background(Color.White),
                                position = "#",           // position
                                teamName = "TEAM",               // Name
                                teamBadge = "",              // icono
                                teamDiff = "+/-",           // segunda columna
                                teamGamesPlayed = "GP",    // primera columna
                                teamPts = "PTS"           // tercera columna
                            )
                        }
                        items(items = viewModel.standings[league.idLeague]!!) { item ->
                            TeamStandingTableItem(
                                modifier = Modifier.background(Color.Transparent),
                                position = item.position,           // position
                                teamName = item.name,               // Name
                                teamBadge = item.icon,              // icono
                                teamDiff = item.seconRow,           // segunda columna
                                teamGamesPlayed = item.firstRow,    // primera columna
                                teamPts = item.thirdRow             // tercera columna
                            )
                        }
                    }
                }
            }
        }
    }


}