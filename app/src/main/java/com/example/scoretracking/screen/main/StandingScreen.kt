package com.example.scoretracking.screen.main

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scoretracking.model.StorageLeague
import com.example.scoretracking.widgets.LeagueFavoriteClicableItem
import com.example.scoretracking.widgets.TeamStandingTableItem

@Composable
fun StandingScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel : MainScreenViewModel = hiltViewModel()
) {

    val filteredEvent = viewModel.standings

    Log.d("SAMBA", "favoriteLeaguesFromStorage: ${viewModel.favoriteLeaguesFromStorage}")

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.favoriteLeaguesFromStorage.isNotEmpty()) {
            Log.d("SAMBA", "favoriteLeaguesFromStorage: ${viewModel.favoriteLeaguesFromStorage}")
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

@Composable
fun CompileLeagueList(leagues : List<StorageLeague>,
                      viewModel: MainScreenViewModel
) {
    Log.d("SAMBA", "COMPILELEAGUELIST WITH $leagues")

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
                .padding(bottom = 15.dp)
                .clickable {
                    isClicked.value = !isClicked.value
                    if (isClicked.value) {
                        viewModel.getStandingByLeague(league.idLeague, league.strCurrentSeason)
                    } else {
                        isClickedForIcon.value = false
                    }
                }) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(39.dp)
                        .padding(start = 20.dp)) {
                    Text(
                        text = league.strLeague,
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 23.sp
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
                Log.d("SAMBA5", "CLICKED AND LOADING LAZY")
                LazyColumn(contentPadding = PaddingValues(4.dp)) {
                    items(items = viewModel.standings[league.idLeague]!!) { item ->
                        TeamStandingTableItem(
                            position = item.intRank,
                            teamName = item.strTeam,
                            teamBadge = item.strTeamBadge,
                            teamDiff = item.intGoalDifference,
                            teamGamesPlayed = item.intPlayed,
                            teamPts = item.intPoints
                        )
                    }
                }
            }
        }
    }


}