package com.example.scoretracking.screen.favoritesScreens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scoretracking.model.Country
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.widgets.BottomNavApp
import com.example.scoretracking.widgets.LeagueItem


@Composable
fun FavoritesSelection(
    navController: NavController,
    favoriteScreenViewModel: FavoriteLeaguesScreenViewModel
) {
    val leagueList = favoriteScreenViewModel.listLeague.collectAsState().value
    val favoritesLeagues = favoriteScreenViewModel.favoriteleagueList.collectAsState().value

    var sportType : String by remember {
        mutableStateOf("Soccer")
    }

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
                        navController.navigate(SportTrackerScreens.SelectFavoritesTeamsScreen.name)
                    }
                }
            )

        },
        bottomBar = {
            BottomNavApp(sportType){
                sportType = it
                favoriteScreenViewModel.loadLeaguesBySport(it)
            }
        }) {
        Column(Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (leagueList.isNotEmpty()) {
                val favoriteSet = mutableSetOf<String>()
                favoritesLeagues.forEach {
                    favoriteSet.add(it.idLeague)
                }
                CompileLeagueList(leagueList, favoriteSet, favoriteScreenViewModel)
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                ) {
                    CircularProgressIndicator()
                    if (sportType == "Soccer") {
                        favoriteScreenViewModel.loadLeaguesBySport("Soccer")
                    }
                }
            }
        }
    }
}

@Composable
fun CompileLeagueList (leagues : List<Country>,
                       favorites : Set<String>,
                       favoriteScreenViewModel: FavoriteLeaguesScreenViewModel) {
    LazyColumn(contentPadding = PaddingValues(4.dp)) {
        items(items = leagues) { item  ->
            item.isFavorite = favorites.contains(item.idLeague)
            LeagueItem(item) {
                favoriteScreenViewModel.saveLeagueClickedAsFavorite(item)
            }
        }
    }
}

@Composable
fun TopAppBarActionButton(
    imageVector: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(imageVector = imageVector, contentDescription = description)
    }
}