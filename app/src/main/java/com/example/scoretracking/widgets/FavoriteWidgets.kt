package com.example.scoretracking.widgets

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.scoretracking.R.drawable as AppDrawable
import com.example.scoretracking.model.thesportdbmodels.Country
import com.example.scoretracking.model.firebasemodels.StorageLeague
import com.example.scoretracking.model.thesportdbmodels.Team
import com.example.scoretracking.screen.favorites.FavoriteTeamsScreenViewModel


/*
 * League's widget for favorites.
 */
@Composable
fun LeagueItem(league : Country,
               onClickAction: () -> Unit) {

    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
            onClickAction.invoke()
        }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = rememberImagePainter("${league.strBadge}/tiny"),
                contentDescription = "League logo",
                modifier = Modifier
                    .padding(8.dp)
                    .height(60.dp)
                    .width(60.dp))
            Column (
                modifier = Modifier
                    .padding(end = 5.dp)
                    .width(180.dp)){
                Text(text = league.strLeague.toString(),
                    style = MaterialTheme.typography.subtitle1,
                    fontSize = 13.sp,)
                Text(text = league.strCountry.toString(),
                    fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(visible = league.isFavorite,
                enter = fadeIn(animationSpec = tween(10),
                                initialAlpha = 0.5f),
                exit = fadeOut(animationSpec = tween(10))) {
                Box(modifier = Modifier.padding(8.dp)) {
                    Image(painter = painterResource(id = AppDrawable.ic_star_favorite),
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
fun LeagueFavoriteClicableItem(league : StorageLeague,
                               favoriteSet : Set<String>,
                               viewModel : FavoriteTeamsScreenViewModel,
                               onClickAction: (Team) -> Unit) {
    var teamsOfLeague = remember {
        viewModel.teamsByLeague
    }

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
                teamsOfLeague = mutableStateListOf()
                isClicked.value = !isClicked.value
                if (isClicked.value) {
                    viewModel.getTeamsByLeague(league.idLeague)
                } else {
                    isClickedForIcon.value = false
                }
            }) {
            Column (
                modifier = Modifier
                    .width(230.dp)
                    .padding(end = 5.dp, start = 20.dp)){
                Text(text = league.strLeague,
                    style = MaterialTheme.typography.subtitle2,
                    fontSize = 20.sp)
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

    teamsOfLeague.forEach { team ->
            isLeagueWithoutTeams.value = false
            if (team.idLeague == league.idLeague && isClicked.value) {
                isClickedForIcon.value = true
                team.isFavorite = favoriteSet.contains(team.idTeam)
                TeamItem(team) {
                    teamSelected = it
                    onClickAction.invoke(teamSelected)
                }
            } else {
                isClickedForIcon.value = false
                isClicked.value = false
            }
    }
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
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(end = 10.dp)) {
                    Text(
                        text = team.strTeam.toString(),
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 13.sp
                    )
                    Text(
                        text = team.strCountry.toString(),
                        fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                AnimatedVisibility(
                    visible = team.isFavorite,
                    enter = fadeIn(initialAlpha = 0.5f),
                    exit = fadeOut()
                ) {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Image(
                            painter = painterResource(id = AppDrawable.ic_star_favorite),
                            contentDescription = "favorite icon"
                        )
                    }
                }
            }
            Divider(startIndent = 8.dp, thickness = 1.dp, color = Color.Black)
        }
}


@Composable
fun TeamStandingTableItem(modifier: Modifier,
                          position : String,
                          teamName : String,
                          teamBadge : String,
                          teamGamesPlayed : String,
                          teamDiff : String,
                          teamPts : String) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .padding(bottom = 5.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .height(40.dp)) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(25.dp)
                .padding(start = 3.dp)) {
            Text(
                text = position,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
                .padding(end = 8.dp)) {
            Image(
                painter = rememberImagePainter("$teamBadge"),
                contentDescription = "League logo",
                modifier = Modifier
                    .padding(1.dp)
                    .height(29.dp)
                    .width(29.dp)
            )
        }
        Box(contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .width(if (teamGamesPlayed.isNotEmpty()) 100.dp else 150.dp)) {
            Text(
                text = teamName,
                textAlign = TextAlign.Start,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.width(40.dp))
        if (teamGamesPlayed.isNotEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(35.dp)
            ) {
                Text(
                    text = teamGamesPlayed,
                    fontStyle = FontStyle.Normal,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
        if (teamDiff.isNotEmpty()) {
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(35.dp)
            ) {
                Text(
                    text = teamDiff,
                    fontStyle = FontStyle.Normal,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.width(5.dp))
        Box(contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .width(40.dp)) {
            Text(
                text = teamPts,
                fontStyle = FontStyle.Normal,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
    }
}


/*
* League's widget for teams favorites.
*/
//@Composable
//fun LeagueClicableItem(league : StorageLeague,
//                       viewModel : MainScreenViewModel
//                       onClickAction: (Team) -> Unit) {
//
//    var teamsOfLeague = remember {
//        viewModel.standings
//    }
//
//    // This is used for the teams lists.
//    val isClicked = remember {
//        mutableStateOf(false)
//    }
//    // This is used only for the icons.
//    val isClickedForIcon = remember {
//        mutableStateOf(false)
//    }
//
//
//    var teamSelected : Team
//    Row(horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.Top,
//        modifier = Modifier
//            .padding(bottom = 8.dp, top = 5.dp)) {
//        Row(verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.clickable {
//                teamsOfLeague = mutableStateMapOf()
//                isClicked.value = !isClicked.value
//                if (isClicked.value) {
//                    viewModel.getStandingByLeague(league.idLeague)
//                } else {
//                    isClickedForIcon.value = false
//                }
//            }) {
//            Column (modifier = Modifier.padding(end = 5.dp, start = 20.dp)){
//                Text(text = league.strLeague,
//                    style = MaterialTheme.typography.subtitle1,
//                    fontSize = 22.sp)
//            }
//            Spacer(modifier = Modifier.weight(1f))
//
//            Crossfade(targetState = isClickedForIcon.value,
//                animationSpec = tween(100)
//            ) { isChecked ->
//                Icon(imageVector = if(isChecked) Icons.Default.KeyboardArrowUp
//                else Icons.Default.KeyboardArrowDown,
//                    contentDescription = "arrow to open",
//                    modifier = Modifier
//                        .height(48.dp)
//                        .width(48.dp)
//                        .padding(end = 15.dp))
//            }
//
//        }
//    }
//
//    teamsOfLeague.forEach { (key, team) ->
//        if (key == league.idLeague && isClicked.value) {
//            isClickedForIcon.value = true
//            TeamItem(team) {
//                teamSelected = it
//                onClickAction.invoke(teamSelected)
//            }
//        } else {
//            isClickedForIcon.value = false
//            isClicked.value = false
//        }
//    }
//}

