package com.example.scoretracking.screen.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.model.Country
import com.example.scoretracking.model.LeaguesModel
import com.example.scoretracking.repository.Resource
import com.example.scoretracking.repository.leagues.LeagueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLeaguesBySport("Soccer").collect {
                when (it) {
                    is Resource.Success-> {
                        _leagueList.value = it.value
                    }
                }
            }
        }
    }
}