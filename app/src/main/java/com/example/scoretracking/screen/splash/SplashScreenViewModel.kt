package com.example.scoretracking.screen.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.model.Country
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.repository.Resource
import com.example.scoretracking.repository.leagues.LeagueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: LeagueRepository
) : ViewModel() {

    private val _leagueList = MutableStateFlow<List<Country>>(emptyList())
    val listLeague = _leagueList.asStateFlow()

    init { loadLeaguesBySport() }

    fun loadLeaguesBySport() {
        viewModelScope.launch() {
            repository.getLeaguesBySport("Soccer").buffer().collectLatest {
                when (it) {
                    is Resource.Success-> {
                        _leagueList.value = it.value
                    }
                    else -> { Log.d("loadLeaguesBySport", "error")}
                }
            }
        }
    }

    fun onAppStart(openAndPopUp : (String, String) -> Unit) {
        openAndPopUp(SportTrackerScreens.SelectFavoritesLeaguesScreen.name, SportTrackerScreens.SplashScreen.name)
    }
}