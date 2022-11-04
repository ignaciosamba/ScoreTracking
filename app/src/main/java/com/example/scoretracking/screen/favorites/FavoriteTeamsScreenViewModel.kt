package com.example.scoretracking.screen.favorites

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.model.firebasemodels.StorageLeague
import com.example.scoretracking.model.firebasemodels.StorageTeam
import com.example.scoretracking.model.services.AccountInterface
import com.example.scoretracking.model.services.LogInterface
import com.example.scoretracking.model.services.StorageFavoriteTeamsInterface
import com.example.scoretracking.model.services.StorageLeagueInterface
import com.example.scoretracking.model.thesportdbmodels.Team
import com.example.scoretracking.repository.Resource
import com.example.scoretracking.repository.teams.TeamsRepository
import com.example.scoretracking.screen.LoginBasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteTeamsScreenViewModel @Inject constructor(
    private val repository: TeamsRepository,
    logService: LogInterface,
    private val storageTeamService: StorageFavoriteTeamsInterface,
    private val storageLeagueService: StorageLeagueInterface,
    private val accountService: AccountInterface,
) : LoginBasicViewModel(logService) {

    var teamsByLeague = mutableStateListOf<Team>()
        private set

    // This list is for all the favorite teams by league
    var favoriteTeamListFromStorage = mutableStateMapOf<String, StorageTeam>()
        private set

    // This list is for all the favorite leagues
    var favoriteLeaguesFromStorage = mutableStateMapOf<String, StorageLeague>()
        private set

    fun addListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageTeamService.addTeamsListener(accountService.getUserId(), ::onDocumentTeamEvent, ::onError)
            storageLeagueService.addLeagueListener(accountService.getUserId(), ::onDocumentLeagueEvent, ::onError)
        }
    }

    fun removeListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageTeamService.removeTeamsListener()
            storageLeagueService.removeLeagueListener()
        }
    }

    fun getTeamsByLeague(leagueId : String) {
        viewModelScope.launch(showErrorExceptionHandler){
            repository.getTeamsByLeagueId(leagueId).distinctUntilChanged().collectLatest { response ->
                when (response) {
                    is Resource.Success -> {
                        // This if it's a workaround for the leagues without teams in it.
                        if (response.value.isNotEmpty()) {
                            teamsByLeague.removeAll(teamsByLeague)
                            teamsByLeague.addAll(response.value.toMutableStateList())
                        } else{
                            teamsByLeague.removeAll(teamsByLeague)
                            onError(Throwable("No teams found in this league."))
                        }
                    }
                    is Resource.Error -> {
                        onError(Throwable(response.error))
                    }
                    else -> {Log.d("checkIfLeagueIsFavorite", "Loading")}
                }
            }
        }
    }

    fun teamClickedToStorage(team: Team) {
        val favoriteTeam = StorageTeam(
            idTeam = team.idTeam,
            strTeamLogo = team.strTeamBadge.toString(),
            strLeague = team.strLeague.toString(),
            strTeam = team.strTeam.toString(),
            userId = accountService.getUserId())

        val saveTeam = !team.isFavorite
        viewModelScope.launch(showErrorExceptionHandler) {
            if (saveTeam) {
                storageTeamService.saveFavoriteTeam(favoriteTeam) { error ->
                    if (error != null) {
                        onError(error)
                    }
                }
            } else {
                val docPath = favoriteTeam.idTeam.plus(favoriteTeam.userId)
                storageTeamService.deleteFavoriteTeam(docPath) { error ->
                    if (error != null) {
                        onError(error)
                    }
                }
            }
        }
    }

    private fun onDocumentTeamEvent(wasDocumentDeleted: Boolean, team : StorageTeam) {
        if (wasDocumentDeleted) {
            favoriteTeamListFromStorage.remove(team.idTeam)
        } else {
            favoriteTeamListFromStorage[team.idTeam] = team
        }
    }

    private fun onDocumentLeagueEvent(wasDocumentDeleted: Boolean, league : StorageLeague) {
        if (wasDocumentDeleted) {
            favoriteLeaguesFromStorage.remove(league.idLeague)
        } else {
            favoriteLeaguesFromStorage[league.idLeague] = league
        }
    }
}