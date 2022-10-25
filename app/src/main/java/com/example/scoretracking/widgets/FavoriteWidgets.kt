package com.example.scoretracking.widgets

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.scoretracking.R
import com.example.scoretracking.model.Country
import com.example.scoretracking.model.LeagueFavorite
import com.example.scoretracking.model.Team
import com.example.scoretracking.screen.favoritesScreens.FavoriteTeamsScreenViewModel


/*
 * League's widget for favorites.
 */
@Composable
fun LeagueItem(league : Country,
                onClickAction: () -> Unit) {

    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onClickAction.invoke()
        }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = rememberImagePainter("${league.strBadge}/tiny"),
                contentDescription = "League logo",
                modifier = Modifier
                    .padding(8.dp)
                    .height(70.dp)
                    .width(70.dp))
            Column (modifier = Modifier.padding(end = 20.dp)){
                Text(text = league.strLeague.toString(),
                    style = MaterialTheme.typography.subtitle1,
                    fontSize = 15.sp,)
                Text(text = league.strCountry.toString())
            }
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(visible = league.isFavorite,
                enter = fadeIn(animationSpec = tween(10),
                                initialAlpha = 0.5f),
                exit = fadeOut(animationSpec = tween(10))) {
                Box(modifier = Modifier.padding(8.dp)) {
                    Image(painter = painterResource(id = R.drawable.ic_star_favorite),
                        contentDescription = "favorite icon")
                }
            }
        }
        Divider(startIndent = 8.dp, thickness = 1.dp, color = Color.Black)
    }

}


/*
 * League's widget for teams favorites.
 */
@Composable
fun LeagueClicableItem(league : LeagueFavorite,
                       favoriteSet : Set<String>,
                        favoriteTeamsViewModel : FavoriteTeamsScreenViewModel,
                        onClickAction: (Team) -> Unit) {

    var teamsOfLeague = favoriteTeamsViewModel.teamsByLeague.collectAsState(null).value
    // This is used for the teams lists.
    val isClicked = remember {
        mutableStateOf(false)
    }
    // This is used only for the icons.
    val isClickedForIcon = remember {
        mutableStateOf(false)
    }

    val isLeagueWithoutTeams = remember{
        mutableStateOf(false)
    }

    var teamSelected : Team
    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(bottom = 8.dp, top = 5.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                teamsOfLeague = emptyList()
                isClicked.value = !isClicked.value
                if (isClicked.value) {
                    favoriteTeamsViewModel.getTeamsByLeague(league.idLeague)
                } else {
                    isClickedForIcon.value = false
                }
            }) {
            Column (modifier = Modifier.padding(end = 5.dp, start = 20.dp)){
                Text(text = league.strLeague,
                    style = MaterialTheme.typography.subtitle1,
                    fontSize = 22.sp)
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
    }

    teamsOfLeague?.forEach { team ->
//        if(team.idLeague == "-99") {
//            isLeagueWithoutTeams.value = true
//        } else {
            isLeagueWithoutTeams.value = false
            if (team.idLeague == league.idLeague && isClicked.value) {
                Log.d("SAMBA", "IF")
                isClickedForIcon.value = true
                Log.d("SAMBA", "TEAM ${team.strTeam} IS FAVORITE: ${favoriteSet.contains(team.idTeam)}")
                team.isFavorite = favoriteSet.contains(team.idTeam)
                TeamItem(team) {
                    teamSelected = it
                    onClickAction.invoke(teamSelected)
                }
            } else {
                isClickedForIcon.value = false
                isClicked.value = false
            }
//        }
    }

    //TODO if the league has no team, we need to show some error message.
//    if (isLeagueWithoutTeams.value && isClicked.value) {
//        Row(horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(start = 45.dp)) {
//            Text(text = "There are no teams in this league.")
//        }
//    }

    Divider(modifier = Modifier.padding(start = 20.dp, end = 20.dp),
        thickness = 1.dp,
        color = Color.Black)
}

/*
 * Team's widget for favorites.
 */
@Composable
fun TeamItem(team: Team,
            onClickAction: (Team) -> Unit) {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClickAction.invoke(team) }
                .padding(start = 25.dp, end = 25.dp, top = 5.dp, bottom = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberImagePainter("${team.strTeamBadge}/preview"),
                    contentDescription = "League logo",
                    modifier = Modifier
                        .padding(8.dp)
                        .height(45.dp)
                        .width(45.dp)
                )
                Column(modifier = Modifier.padding(end = 20.dp)) {
                    Text(
                        text = team.strTeam.toString(),
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 15.sp
                    )
                    Text(text = team.strCountry.toString())
                }
                Spacer(modifier = Modifier.weight(1f))
                AnimatedVisibility(
                    visible = team.isFavorite,
                    enter = fadeIn(initialAlpha = 0.5f),
                    exit = fadeOut()
                ) {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_star_favorite),
                            contentDescription = "favorite icon"
                        )
                    }
                }
            }
            Divider(startIndent = 8.dp, thickness = 1.dp, color = Color.Black)
        }
}

