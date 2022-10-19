package com.example.scoretracking.widgets

import android.service.autofill.OnClickAction
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.scoretracking.R
import com.example.scoretracking.model.Country
import com.example.scoretracking.model.LeagueFavorite
import com.example.scoretracking.model.Team
import com.example.scoretracking.screen.favoritesScreens.FavoriteTeamsScreenViewModel


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
            Column (modifier = Modifier.padding(end = 50.dp)){
                Text(text = league.strLeague.toString(), style = MaterialTheme.typography.subtitle1, fontSize = 15.sp)
                Text(text = league.strCountry.toString())
            }
            Spacer(modifier = Modifier.weight(1f))
            val density = LocalDensity.current
            AnimatedVisibility(visible = league.isFavorite,
                enter = fadeIn(initialAlpha = 0.5f),
                exit = fadeOut()) {
                Box(modifier = Modifier.padding(8.dp)) {
                    Image(painter = painterResource(id = R.drawable.ic_star_favorite),
                        contentDescription = "favorite icon")
                }
            }
        }
        Divider(startIndent = 8.dp, thickness = 1.dp, color = Color.Black)
    }

}


@Composable
fun LeagueClicableItem(league : LeagueFavorite,
                       favoriteSet : Set<String>,
                        favoriteTeamsViewModel : FavoriteTeamsScreenViewModel,
                        onClickAction: (Team) -> Unit) {

    var teamsOfLeague = favoriteTeamsViewModel.teamsByLeague.collectAsState().value
    var isClicked = remember {
        mutableStateOf(false)
    }
    var isClickedForIcon = remember {
        mutableStateOf(false)
    }
    var teamSelected : Team
    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(bottom = 8.dp, top = 5.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                isClicked.value = !isClicked.value
                Log.d("SAMBA444", "onClick ${league.strLeague} with: ${isClicked.value}")
                teamsOfLeague = emptyList()
                favoriteTeamsViewModel.getTeamsByLeague(league.idLeague)
            }) {
            Column (modifier = Modifier.padding(end = 5.dp, start = 20.dp)){
                Text(text = league.strLeague, style = MaterialTheme.typography.subtitle1, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = if(isClickedForIcon.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "arrow to open",
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
                    .padding(end = 15.dp))
        }
    }

    teamsOfLeague.forEach { team ->
        if (team.idLeague == league.idLeague && isClicked.value) {
            Log.d("SAMBA444", "TRUE : ${league.strLeague} WITH ${team.strTeam}")
            isClickedForIcon.value = true
            team.isFavorite = favoriteSet.contains(team.idTeam)
            TeamItem(team, isClicked.value) {
                Log.d("SAMBA445", "ONCLICK TEAM : ${league.strLeague}")
                teamSelected = it
                onClickAction.invoke(teamSelected)
            }
        } else {
            Log.d("SAMBA444", "FALSE : ${league.strLeague} WITH ${team.strTeam}")
            isClickedForIcon.value = false
        }
    }

    Divider(modifier = Modifier.padding(start = 20.dp, end = 20.dp),
        thickness = 1.dp,
        color = Color.Black)
}


@Composable
fun TeamItem(team: Team ,
            isClicked : Boolean,
            onClickAction: (Team) -> Unit) {
    Log.d("SAMBA22", "THE : ${team.strTeam} WITH ${team.idTeam}")
    /*AnimatedVisibility(visible = isClicked,
        enter = fadeIn(initialAlpha = 0.5f),
        exit = fadeOut()) {*/
        Log.d("SAMBA88", "UNA COSA ${team.strTeam}")
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    onClickAction.invoke(team)
                }
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
                Column(modifier = Modifier.padding(end = 50.dp)) {
                    Text(
                        text = team.strTeam.toString(),
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 15.sp
                    )
                    Text(text = team.strCountry.toString())
                }
                Spacer(modifier = Modifier.weight(1f))
                val density = LocalDensity.current
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
//    }
}

