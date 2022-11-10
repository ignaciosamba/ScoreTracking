package com.example.scoretracking.screen.favorites

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scoretracking.R
import com.example.scoretracking.model.thesportdbmodels.Country
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.widgets.BottomNavFavoritesApp
import com.example.scoretracking.widgets.LeagueItem
import kotlinx.coroutines.delay
import java.util.*


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FavoritesSelection(
    openScreen: (String) -> Unit,
    fromScreen: String?,
    modifier: Modifier = Modifier,
    viewModel: FavoriteLeaguesScreenViewModel = hiltViewModel()
) {

    DisposableEffect(viewModel) {
        viewModel.addListener()
        onDispose { viewModel.removeListener() }
    }

    val leagueList = viewModel.listLeague.collectAsState().value

    val filterList = remember {
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

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    // LaunchedEffect prevents endless focus request
    LaunchedEffect(searchClicked) {
        if (searchClicked) {
            focusRequester.requestFocus()
            delay(10) // Make sure you have delay here
            keyboard?.show()
        }
    }

    val activity = (LocalContext.current as? Activity)

    BackHandler (enabled = fromScreen.equals("splash", ignoreCase = true) ||
            fromScreen.equals("login", ignoreCase = true)) {
        if (searchClicked) {
            searchClicked = false
            text = ""
            filterList.value = emptyList()
            focusRequester.requestFocus()
            keyboard?.hide()
        } else if (fromScreen.equals("splash", ignoreCase = true) ||
            fromScreen.equals("login", ignoreCase = true)) {
            activity?.finish()
        }
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
                    if (fromScreen.equals("splash", ignoreCase = true) ||
                        fromScreen.equals("login", ignoreCase = true)) {
                        TopAppBarActionButton(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            description = "GoToFavoriteTeams"
                        ) {
                            viewModel.cleanLeaguesList()
                            filterList.value = emptyList()
                            openScreen(SportTrackerScreens.SelectFavoritesTeamsScreen.name)
                        }
                    }
                },
                contentColor = Color.Black,
                backgroundColor = Color.White
            )
        },
        bottomBar = {
            BottomNavFavoritesApp(sportType){
                searchClicked = false
                text = ""
                filterList.value = emptyList()
                sportType = it
                viewModel.loadLeaguesBySport(it)
            }
        }) {
        Column(verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
            if (searchClicked) {
                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .height(50.dp)
                        .background(colorResource(id = R.color.white))
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                ) {
                        Card(modifier = Modifier
                            .width(300.dp)
                            .height(40.dp),
                            shape = CircleShape,
                            border = BorderStroke(width = 1.dp, color = Color.Transparent),
                            backgroundColor = Color.White) {
                            BasicTextField(value = text,
                                onValueChange = {
                                    text = it
                                    filterList.value = leagueList.filter { league ->
                                        league.strLeague?.lowercase(Locale.ROOT)
                                            ?.contains(text.lowercase(Locale.ROOT))!!
                                    }
                                },
                                modifier = Modifier
                                    .background(Color.White)
                                    .fillMaxWidth()
                                    .padding(top = 5.dp, start = 15.dp)
                                    .focusRequester(focusRequester),
                                textStyle = TextStyle(fontSize = 20.sp),
                                cursorBrush = SolidColor(Color.Gray),
                                keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                            )
                        }
                }
            }
            if (leagueList.isNotEmpty()) {
                val favoriteSet = mutableSetOf<String>()
                viewModel.favoriteLeagueListFromStorage.forEach {
                    favoriteSet.add(it.value.idLeague)
                }
                if (filterList.value.isNotEmpty()) {
                    CompileLeagueList(filterList.value, favoriteSet, viewModel)
                } else {
                    CompileLeagueList(leagueList, favoriteSet, viewModel)
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
                    if (sportType == "Soccer") {
                        viewModel.loadLeaguesBySport("Soccer")
                    }
                }
            }
        }
    }
}

@Composable
fun CompileLeagueList (leagues : List<Country>,
                       favorites : Set<String>,
                       viewModel: FavoriteLeaguesScreenViewModel) {
    val mutablelist = leagues.toMutableList()
    mutablelist.forEach {
        it.isFavorite = favorites.contains(it.idLeague)
    }
    LazyColumn(contentPadding = PaddingValues(4.dp)) {
        items(items = mutablelist.sortedBy { !it.isFavorite }) { item  ->
            LeagueItem(item) {
//                viewModel.saveLeagueClickedAsFavorite(item)
                viewModel.updateStorageAfterClick(item)
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