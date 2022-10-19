package com.example.scoretracking.screen.favoritesScreens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.model.Country
import com.example.scoretracking.model.LeagueFavorite
import com.example.scoretracking.repository.Resource
import com.example.scoretracking.repository.leagues.LeagueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteLeaguesScreenViewModel @Inject constructor(
    private val repository: LeagueRepository
) : ViewModel() {

    // This list is for all the leagues by sport
    private var _leagueList = MutableStateFlow<List<Country>>(emptyList())
    val listLeague = _leagueList.asStateFlow()

    // This list is for all the favorite leagues by sport
    private var _favoriteleagueList = MutableStateFlow<List<LeagueFavorite>>(emptyList())
    val favoriteleagueList = _favoriteleagueList.asStateFlow()

    fun loadLeaguesBySport(sportType: String) {
        viewModelScope.launch(Dispatchers.IO){
            repository.getFavoritesLeaguesBySport(sportType).combine(repository.getLeaguesBySport(sportType)) {
                favoriteListe, leagueListe ->
                checkIfLeagueIsFavorite(favoriteListe, leagueListe)
            }.collect()
        }
    }

    fun saveLeagueClickedAsFavorite(league : Country) {
        val favoriteLeague = LeagueFavorite(
            idLeague = league.idLeague,
            strLeagueAlternate = league.strLeagueAlternate.toString(),
            strLeague = league.strLeague.toString(),
            strSport = league.strSport.toString())
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveTeamsByLeagueIntoFavoritesDB(league.idLeague).collect()
            repository.saveLeagueIntoFavoritesDB(favoriteLeague, !league.isFavorite)
        }
    }

    private fun checkIfLeagueIsFavorite(favorites : List<LeagueFavorite>, leagues :  Resource<List<Country>>) {
        when (leagues) {
            is Resource.Success -> {
                _favoriteleagueList.value = favorites
                _leagueList.value = leagues.value
            }
        }
    }
}