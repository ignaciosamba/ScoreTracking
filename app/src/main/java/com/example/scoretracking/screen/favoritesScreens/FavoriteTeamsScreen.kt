package com.example.scoretracking.screen.favoritesScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scoretracking.model.Country
import com.example.scoretracking.model.Team
import com.example.scoretracking.widgets.BottomNavApp
import com.example.scoretracking.widgets.LeagueItem
import com.example.scoretracking.widgets.TeamItem


@Composable
fun FavoritesTeamsSelection(
    navController: NavController,
    favoriteTeamsScreenViewModel: FavoriteTeamsScreenViewModel
) {

    val teamsList = favoriteTeamsScreenViewModel.teamsByLeague.collectAsState().value
    val favoritesTeams = favoriteTeamsScreenViewModel.favoriteTeamList.collectAsState().value

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
            ) }
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (teamsList.isNotEmpty()) {
                val favoriteSet = mutableSetOf<String>()
                favoritesTeams.forEach {
                    favoriteSet.add(it.idTeam)
                }
                CompileLeagueList(teamsList, favoriteSet, favoriteTeamsScreenViewModel)
                teamsList.forEach {
                    Log.d("SAMBA77", "FOR")
                    it.strTeam?.let { it1 -> Text(text = it1) }
                }
            } else {
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
}


@Composable
fun CompileLeagueList (leagues : List<Team>,
                       favorites : Set<String>,
                       favoriteScreenViewModel: FavoriteTeamsScreenViewModel) {
    LazyColumn(contentPadding = PaddingValues(4.dp)) {
        items(items = leagues) { item  ->
            item.isFavorite = favorites.contains(item.idLeague)
            TeamItem(item) {
                favoriteScreenViewModel.saveTeamClickedAsFavorite(item)
//                favoriteScreenViewModel.saveLeagueClickedAsFavorite(item)
            }
        }
    }
}