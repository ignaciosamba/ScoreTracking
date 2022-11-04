package com.example.scoretracking.screen.favorites

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.model.thesportdbmodels.Country
import com.example.scoretracking.model.firebasemodels.StorageLeague
import com.example.scoretracking.model.services.AccountInterface
import com.example.scoretracking.model.services.LogInterface
import com.example.scoretracking.model.services.StorageLeagueInterface
import com.example.scoretracking.repository.Resource
import com.example.scoretracking.repository.leagues.LeagueRepository
import com.example.scoretracking.screen.LoginBasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteLeaguesScreenViewModel @Inject constructor(
    private val repository: LeagueRepository,
    logService: LogInterface,
    private val storageService: StorageLeagueInterface,
    private val accountService: AccountInterface,
) : LoginBasicViewModel(logService) {

    // This list is for all the leagues by sport
    private var _leagueList = MutableStateFlow<List<Country>>(emptyList())
    val listLeague = _leagueList.asStateFlow()

    // This list is for all the favorite teams by league
    var favoriteLeagueListFromStorage = mutableStateMapOf<String, StorageLeague>()
        private set

    var leagueSelectedFromView = ""

    fun cleanLeaguesList () {
        _leagueList.value = emptyList()
    }

    fun addListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageService.addLeagueListener(accountService.getUserId(), ::onDocumentEvent, ::onError)
        }
    }

    fun removeListener() {
        viewModelScope.launch(showErrorExceptionHandler) { storageService.removeLeagueListener() }
    }

    fun loadLeaguesBySport(sportType: String) {
        leagueSelectedFromView = sportType
        viewModelScope.launch(){
            repository.getLeaguesBySport(sportType).collect() { response ->
                when (response) {
                    is Resource.Success -> {
                        if (response.value.isNotEmpty() &&
                            response.value[0].strSport == leagueSelectedFromView) {
                            _leagueList.value = response.value
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


    fun updateStorageAfterClick(league : Country) {
        val storageLeague = StorageLeague(
            idLeague = league.idLeague,
            strLeagueAlternate = league.strLeagueAlternate.toString(),
            strCurrentSeason = league.strCurrentSeason.toString(),
            strLeague = league.strLeague.toString(),
            strSport = league.strSport.toString(),
            userId = accountService.getUserId())
        val saveLeague = !league.isFavorite
        if (saveLeague) {
            storageService.saveFavoriteLeague(storageLeague) { error ->
                if (error != null) onError(error)
            }
        } else {
            val docPath = storageLeague.idLeague.plus(storageLeague.userId)
            storageService.deleteFavoriteLeague(docPath) { error ->
                if (error != null) onError(error)
            }
        }
    }

    private fun onDocumentEvent(wasDocumentDeleted: Boolean, team : StorageLeague) {
        if (wasDocumentDeleted) {
            favoriteLeagueListFromStorage.remove(team.idLeague)
        } else {
            favoriteLeagueListFromStorage[team.idLeague] = team
        }
    }
}