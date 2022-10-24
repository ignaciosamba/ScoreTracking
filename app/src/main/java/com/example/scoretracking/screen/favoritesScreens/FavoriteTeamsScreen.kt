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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.scoretracking.model.LeagueFavorite
import com.example.scoretracking.widgets.LeagueClicableItem


@Composable
fun FavoritesTeamsSelection(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    favoriteTeamsScreenViewModel: FavoriteTeamsScreenViewModel = hiltViewModel()) {
    val favoriteLeagues = favoriteTeamsScreenViewModel.favoriteleagueList.collectAsState().value
    val favoritesTeams = favoriteTeamsScreenViewModel.favoriteTeamList.collectAsState().value

    val context = LocalContext.current
    Scaffold(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Select your favorite teams") },
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (favoriteLeagues.isNotEmpty()) {
                val favoriteSet = mutableSetOf<String>()
                favoritesTeams.forEach {
                    favoriteSet.add(it.idTeam)
                }
                CompileLeagueList(favoriteLeagues, favoriteSet, favoriteTeamsScreenViewModel)
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
}


@Composable
fun CompileLeagueList (leagues : List<LeagueFavorite>,
                       favorites : Set<String>,
                       favoriteScreenViewModel: FavoriteTeamsScreenViewModel) {
    LazyColumn(contentPadding = PaddingValues(4.dp)) {
        items(items = leagues) { item  ->
            LeagueClicableItem(item, favorites, favoriteScreenViewModel) { team ->
                favoriteScreenViewModel.saveTeamClickedAsFavorite(team)
            }
        }
    }
}