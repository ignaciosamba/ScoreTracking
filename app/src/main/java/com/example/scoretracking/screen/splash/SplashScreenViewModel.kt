package com.example.scoretracking.screen.splash

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.model.firebasemodels.StorageLeague
import com.example.scoretracking.model.thesportdbmodels.Country
import com.example.scoretracking.model.services.AccountInterface
import com.example.scoretracking.model.thesportdbmodels.Team
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.repository.Resource
import com.example.scoretracking.repository.leagues.LeagueRepository
import com.example.scoretracking.repository.teams.TeamsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: LeagueRepository,
    private val teamsRepository: TeamsRepository,
    private val account: AccountInterface,
) : ViewModel() {

    private val _leagueList = MutableStateFlow<List<Country>>(emptyList())
    val listLeague = _leagueList.asStateFlow()

//    private val _teamList = MutableStateFlow<List<Team>>(emptyList())
//    val teamList = _teamList.asStateFlow()


    init { loadLeaguesBySport() }

    fun loadLeaguesBySport() {
        viewModelScope.launch {
            val soccerFlow = repository.getLeaguesBySport("Soccer")
            val basketFlow = repository.getLeaguesBySport("Basketball")
            val motorRacingFlow = repository.getLeaguesBySport("Motorsport")
            soccerFlow.zip(basketFlow) { soccer, basket ->
                when (basket) {
                    is Resource.Success -> _leagueList.value = basket.value
                    else -> Log.d("loadLeaguesBySport", "error")
                }
                when (soccer) {
                    is Resource.Success -> _leagueList.value = soccer.value
                    else -> Log.d("loadLeaguesBySport", "error")
                }
            }.zip(motorRacingFlow) { _, motorRacing ->
                when (motorRacing) {
                    is Resource.Success -> _leagueList.value = motorRacing.value
                    else -> Log.d("loadLeaguesBySport", "error")
                }
            }.collect()
        }
    }

    fun onAppStart(openAndPopUp : (String, String) -> Unit) {
        if (account.hasUser()) {
            //TODO we need to check if the user has favorites, if not we need to open the favorite Screen.
            openAndPopUp(
                SportTrackerScreens.GamesScreen.name,
                SportTrackerScreens.SplashScreen.name
            )
        }else {
            openAndPopUp(
                SportTrackerScreens.LoginScreen.name,
                SportTrackerScreens.SplashScreen.name
            )
        }
    }
}