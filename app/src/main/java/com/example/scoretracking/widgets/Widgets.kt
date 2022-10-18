package com.example.scoretracking.widgets

import android.service.autofill.OnClickAction
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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


@Composable
fun LeagueItem(league : Country,
                onClickAction: () -> Unit) {

    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onClickAction.invoke()
        }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = rememberImagePainter(league.strBadge),
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
fun TeamItem(team: Team ,
               onClickAction: () -> Unit) {

    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onClickAction.invoke()
        }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = rememberImagePainter(team.strTeamBadge),
                contentDescription = "League logo",
                modifier = Modifier
                    .padding(8.dp)
                    .height(70.dp)
                    .width(70.dp))
            Column (modifier = Modifier.padding(end = 50.dp)){
                Text(text = team.strTeam.toString(), style = MaterialTheme.typography.subtitle1, fontSize = 15.sp)
                Text(text = team.strCountry.toString())
            }
            Spacer(modifier = Modifier.weight(1f))
            val density = LocalDensity.current
            AnimatedVisibility(visible = team.isFavorite,
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