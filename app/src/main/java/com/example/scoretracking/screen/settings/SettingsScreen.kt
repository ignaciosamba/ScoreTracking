package com.example.scoretracking.screen.settings

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scoretracking.R
import com.example.scoretracking.navigation.SportTrackerScreens

@Composable
fun SettingsScreen(
    openScreen: (String) -> Unit,
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    Scaffold(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
                backgroundColor = colorResource(id = R.color.white)
            )
        }) {
        
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) {

            ItemSetting(icon = R.drawable.ic_trophy, name = "Select favorite leagues") {
                openScreen("${SportTrackerScreens.SelectFavoritesLeaguesScreen.name}/settings")
            }
            ItemSetting(icon = R.drawable.ic_team, name = "Select favorite teams") {
                openScreen(SportTrackerScreens.SelectFavoritesTeamsScreen.name)
            }
            ItemSetting(icon = R.drawable.ic_log_out, name = "Log out") {
                viewModel.logOut(openAndPopUp)
            }
        }
        
    }
}

@Composable
fun ItemSetting(icon : Int,
                name : String,
                onClick : () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 15.dp, end = 15.dp)
            .clickable {
                onClick()
            }
    ) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(end = 8.dp)
        ) {
            Image(painter = painterResource(id = icon), contentDescription = "item setting icon")
        }
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .wrapContentWidth()
                .height(48.dp)) {
            Text(text = name, fontSize = 20.sp, textAlign = TextAlign.Start)
        }
    }
}