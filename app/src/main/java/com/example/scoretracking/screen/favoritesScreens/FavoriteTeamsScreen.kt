package com.example.scoretracking.screen.favoritesScreens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scoretracking.widgets.BottomNavApp


@Composable
fun FavoritesTeamsSelection(
    navController: NavController
) {

    val context = LocalContext.current

    Scaffold(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Select your favorite leagues") },
                actions = {
                    // GO icon
                    TopAppBarActionButton(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        description = "Search"
                    ) {
                        Toast.makeText(context, "GO Click", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            )

        }) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                ) {
                    CircularProgressIndicator()
//                    if (sportType == "Soccer") {
//                        favoriteScreenViewModel.loadLeaguesBySport("Soccer")
//                    }
                }
            }
        }
}