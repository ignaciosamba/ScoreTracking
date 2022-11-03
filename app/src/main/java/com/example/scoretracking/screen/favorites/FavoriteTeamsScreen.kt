package com.example.scoretracking.screen.favorites

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scoretracking.model.StorageLeague
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.widgets.LeagueFavoriteClicableItem


@Composable
fun FavoritesTeamsSelection(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel : FavoriteTeamsScreenViewModel = hiltViewModel()) {

    DisposableEffect(viewModel) {
        viewModel.addListener()
        onDispose { viewModel.removeListener() }
    }

    val favoriteLeagues = viewModel.favoriteLeaguesFromStorage
    val favoritesTeamsFromStorage = viewModel.favoriteTeamListFromStorage

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
                        openScreen(SportTrackerScreens.GamesScreen.name)
                    }
                },
                contentColor = Color.Black,
                backgroundColor = Color.White,
                elevation = 5.dp
            ) }
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (favoriteLeagues.isNotEmpty()) {
                val favoriteSet = mutableSetOf<String>()
                favoritesTeamsFromStorage.forEach {
                    favoriteSet.add(it.value.idTeam)
                }
                CompileLeagueList(favoriteLeagues.values.toList(), favoriteSet, viewModel)
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
fun CompileLeagueList (leagues : List<StorageLeague>,
                       favorites : Set<String>,
                       viewModel: FavoriteTeamsScreenViewModel) {
    LazyColumn(contentPadding = PaddingValues(4.dp)) {
        items(items = leagues) { item  ->
            LeagueFavoriteClicableItem(item, favorites, viewModel) { team ->
                viewModel.teamClickedToStorage(team)
            }
        }
    }
}