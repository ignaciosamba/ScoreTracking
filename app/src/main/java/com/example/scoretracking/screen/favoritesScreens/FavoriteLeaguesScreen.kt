package com.example.scoretracking.screen.favoritesScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scoretracking.R
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
    val context = LocalContext.current

    var filterList = remember {
        mutableStateOf(leagueList)
    }

    var sportType : String by remember {
        mutableStateOf("Soccer")
    }

    var searchClicked : Boolean by remember {
        mutableStateOf(false)
    }

    var text by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Select your favorite leagues") },
                actions = {
                    // SEARCH button
                    TopAppBarActionButton(
                        imageVector = Icons.Default.Search,
                        description = "Search League") {
                        searchClicked = true
                    }
                    // GO icon
                    TopAppBarActionButton(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        description = "GoToFavoriteTeams"
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (searchClicked) {
                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .height(50.dp)
                        .background(colorResource(id = R.color.purple_500))
                        .fillMaxWidth()
                ) {
                        Card(modifier = Modifier
                            .width(300.dp)
                            .height(40.dp),
                            shape = CircleShape) {
                            TextField(value = text,
                                onValueChange = {
                                    text = it
                                    Log.d("SAMBA55", text)
                                    filterList.value = leagueList.filter { league -> league.strLeague?.contains(text)!! }
                                    Log.d("SAMBA55", "league ${filterList.value.size}")
                                },
                                modifier = Modifier.background(Color.White)
                            )
                        }
                }
            }
            if (leagueList.isNotEmpty()) {
                Log.d("SAMBA55", "league 22 ${filterList.value.size}")
                val favoriteSet = mutableSetOf<String>()
                favoritesLeagues.forEach {
                    favoriteSet.add(it.idLeague)
                }
                if (filterList.value.isNotEmpty())
                    CompileLeagueList(filterList.value, favoriteSet, favoriteScreenViewModel)
                else
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
    val mutablelist = leagues.toMutableList()
    mutablelist.forEach {
        it.isFavorite = favorites.contains(it.idLeague)
    }
    LazyColumn(contentPadding = PaddingValues(4.dp)) {
        items(items = mutablelist.sortedBy { !it.isFavorite }) { item  ->
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