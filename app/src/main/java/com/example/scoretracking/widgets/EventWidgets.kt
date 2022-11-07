package com.example.scoretracking.widgets

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.scoretracking.R
import com.example.scoretracking.model.thesportdbmodels.Event
import com.example.scoretracking.screen.main.MainScreenViewModel

@Composable
fun GameEventCard(event : Event,
                  viewModel : MainScreenViewModel,
                  onClick: (String) ->  Unit) {
    Card(
        modifier = Modifier
            .padding(bottom = 15.dp, end = 10.dp, start = 10.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable { onClick.invoke(event.idEvent.toString()) },
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp,
        backgroundColor = colorResource(id = R.color.white_dark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            viewModel.getTeamBadge(event.idHomeTeam)
            Spacer(modifier = Modifier.width(10.dp))
            ItemTeam(event.strHomeTeam, event.idHomeTeam, viewModel)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = event.intHomeScore ?: "",
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = viewModel.getFinalOrDateText(event),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = event.intAwayScore ?: "",
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            viewModel.getTeamBadge(event.idAwayTeam)
            ItemTeam(
                teamName = event.strAwayTeam,
                teamId = event.idAwayTeam,
                viewModel = viewModel
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}

@Composable
fun MotorSportEventCard(event : Event,
                        viewModel : MainScreenViewModel,
                        onClick: (String) ->  Unit) {
    Card(modifier = Modifier
        .padding(bottom = 15.dp, end = 10.dp, start = 10.dp)
        .fillMaxWidth()
        .fillMaxHeight()
        .clickable { onClick.invoke(event.idEvent.toString()) },
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp,
        backgroundColor = colorResource(id = R.color.white_dark)
    ) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()) {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(90.dp)) {
                Image(
                    painter = rememberImagePainter("${event.strBanner}/preview"),
                    contentDescription = "Team logo",
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillBounds
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)) {
                Text(
                    text = "${event.strEvent}",
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)) {
                Text(
                    text = viewModel.getFinalOrDateText(event),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }
    }
}


@Composable
fun ItemTeam(
    teamName : String?,
    teamId: String?,
    viewModel: MainScreenViewModel
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(55.dp)
                .height(55.dp)
                .background(color = Color.Transparent, shape = CircleShape)) {
            if (viewModel.teamBadge[teamId].isNullOrEmpty()) {
                Log.d("SAMBA", "CALLING TE API FOR BADGE")
                viewModel.getTeamBadgeFromApi(teamName = teamName!!)
            }
            Image(
                painter = rememberImagePainter("${viewModel.teamBadge[teamId]}/preview"),
                contentDescription = "Team logo",
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .align(Alignment.TopCenter)
            )
        }
        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .wrapContentHeight()
                .width(65.dp)) {
            Text(text = teamName.toString(), fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}