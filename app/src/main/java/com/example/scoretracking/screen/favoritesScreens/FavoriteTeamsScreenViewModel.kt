package com.example.scoretracking.screen.favoritesScreens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.model.LeagueFavorite
import com.example.scoretracking.model.Team
import com.example.scoretracking.model.TeamsFavorite
import com.example.scoretracking.repository.Resource
import com.example.scoretracking.repository.teams.TeamsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteTeamsScreenViewModel @Inject constructor(
    private val repository: TeamsRepository
) : ViewModel() {

    private var _teamsByLeague = MutableStateFlow<List<Team>>(emptyList())
    val teamsByLeague = _teamsByLeague.asStateFlow()

    // This list is for all the favorite teams by league
    private var _favoriteTeamList = MutableStateFlow<List<TeamsFavorite>>(emptyList())
    val favoriteTeamList = _favoriteTeamList.asStateFlow()

    // This list is for all the favorite leagues by sport
    private var _favoriteleagueList = MutableStateFlow<List<LeagueFavorite>>(emptyList())
    val favoriteleagueList = _favoriteleagueList.asStateFlow()

    var leagueSelectedFromView = ""

    init {
        getFavoriteLeagues()
    }

    fun getFavoriteLeagues() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavoriteLeagues().collect() {
                _favoriteleagueList.value = it
            }
        }
    }

    fun getTeamsByLeague(leagueId : String) {
        leagueSelectedFromView = leagueId
        viewModelScope.launch(){
            repository.getFavoritesTeams().combine(repository.getTeamsByLeagueId(leagueId)) {
                    favoriteListe, teamListe ->
                checkIfLeagueIsFavorite(favoriteListe, teamListe)
            }.collect()
        }
    }

    fun saveTeamClickedAsFavorite(team : Team) {
        val favoriteTeam = TeamsFavorite(
            idTeam = team.idTeam,
            strTeamLogo = team.strTeamBadge.toString(),
            strLeague = team.strLeague.toString(),
            strTeam = team.strTeam.toString())
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLeagueIntoFavoritesDB(favoriteTeam, !team.isFavorite)
        }
    }

    private fun checkIfLeagueIsFavorite(favorites : List<TeamsFavorite>,
                                        teams : Resource<List<Team>>) {
        when (teams) {
            is Resource.Success -> {
                // This if it's a workaround for the auto trigger from Room and Flow.
                // avoids the 2nd update(trigger).
                if (teams.value.isNotEmpty() &&
                    teams.value[0].idLeague == leagueSelectedFromView) {
                    _favoriteTeamList.value = favorites
                    _teamsByLeague.value = teams.value
                } else if (teams.value.isEmpty() && leagueSelectedFromView != "-99") {
                    _favoriteTeamList.value = favorites
                    _teamsByLeague.value = listOf(Team(idLeague = "-99"))
                }
            }
            else -> { Log.d("checkIfLeagueIsFavorite", "Error")}
        }
    }
}