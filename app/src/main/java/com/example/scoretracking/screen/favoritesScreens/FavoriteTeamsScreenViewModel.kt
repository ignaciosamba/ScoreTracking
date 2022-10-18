package com.example.scoretracking.screen.favoritesScreens

import androidx.lifecycle.ViewModel
import com.example.scoretracking.repository.leagues.LeagueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class FavoriteTeamsScreenViewModel @Inject constructor(
    private val repository: LeagueRepository
) : ViewModel() {

//    private var _teamsByLeague = MutableStateFlow<List<Country>>(emptyList())
//    val teamsByLeague = _teamsByLeague.asStateFlow()
}