package com.example.scoretracking.screen.main

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.commons.getTimeZoneOffsetWithUTCInHours
import com.example.scoretracking.model.Event
import com.example.scoretracking.model.StorageLeague
import com.example.scoretracking.model.StorageTeam
import com.example.scoretracking.model.Table
import com.example.scoretracking.model.service.AccountInterface
import com.example.scoretracking.model.service.LogInterface
import com.example.scoretracking.model.service.StorageFavoriteTeamsInterface
import com.example.scoretracking.model.service.StorageLeagueInterface
import com.example.scoretracking.repository.Resource
import com.example.scoretracking.repository.main.GamesRepository
import com.example.scoretracking.screen.LoginBasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: GamesRepository,
    logService: LogInterface,
    private val storageLeagueService: StorageLeagueInterface,
    private val storageFavoriteTeamsInterface: StorageFavoriteTeamsInterface,
    private val accountService: AccountInterface
) : LoginBasicViewModel(logService) {

    // This map is for all the events.
    var events = mutableStateMapOf<String, List<Event>>()
        private set
    // This map is for all the teams icons.
    var teamBadge = mutableStateMapOf<String, String>()
        private set
    // This list is for all the favorite leagues
    var favoriteLeaguesFromStorage = mutableStateMapOf<String, StorageLeague>()
        private set
    // This list is for all the favorite leagues
    private var favoriteTeamsFromStorage = mutableStateMapOf<String, StorageTeam>()

    var selectedDay = mutableStateOf<LocalDate>(LocalDate.now())
        private set

    // This map is for all the standings tables.
    var standings = mutableStateMapOf<String, List<Table>>()
        private set

    init {
        getEventsByDate()
    }

    fun addListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageLeagueService.addLeagueListener(accountService.getUserId(), ::onDocumentLeagueEvent, ::onError)
            storageFavoriteTeamsInterface.addTeamsListener(accountService.getUserId(), ::onDocumentTeamsEvent, ::onError)
        }
    }

    fun removeListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageLeagueService.removeLeagueListener()
            storageFavoriteTeamsInterface.removeTeamsListener()
        }
    }

    fun getEventsByDate(date : LocalDate = LocalDate.now()) {
        viewModelScope.launch(showErrorExceptionHandler) {
            repository.getEventsByDay(date.toString()).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        favoriteLeaguesFromStorage.forEach { (idLeague, league) ->
                            val eventList = response.value.events.filter { event ->
                                idLeague == event.idLeague && event.dateEvent == date.toString()
                            }
                            if (eventList.isNotEmpty()) events[league.strLeague] = eventList.sortedBy { it.strTimestamp }
                        }
                        favoriteTeamsFromStorage.forEach { (idTeam, team) ->
                            val eventList = response.value.events.filter { event ->
                                (idTeam == event.idAwayTeam || idTeam == event.idHomeTeam) && event.dateEvent == date.toString()
                            }
                            if (eventList.isNotEmpty() && !events.containsKey(team.strLeague)) events[eventList[0].strLeague?: ""] = eventList.sortedBy { it.strTimestamp }
                        }
                    }
                    is Resource.Loading -> {
                        Log.d("getEventsByDate", "LOADING")
                    }
                    is Resource.Error -> {
                        onError(error = Throwable(response.error))
                    }
                }
            }
        }
    }

    private fun onDocumentLeagueEvent(wasDocumentDeleted: Boolean, league : StorageLeague) {
        if (wasDocumentDeleted) {
            favoriteLeaguesFromStorage.remove(league.idLeague)
        } else {
            favoriteLeaguesFromStorage[league.idLeague] = league
        }
    }

    private fun onDocumentTeamsEvent(wasDocumentDeleted: Boolean, team: StorageTeam) {
        if (wasDocumentDeleted) {
            favoriteTeamsFromStorage.remove(team.idTeam)
        } else {
            favoriteTeamsFromStorage[team.idTeam] = team
        }
    }

    fun getTeamBadge(idTeam : String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTeamBadge(idTeam?:"").collect(){
                teamBadge[idTeam?:""] = it
            }
        }
    }

    fun getTeamBadgeFromApi(teamName : String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTeamBadgeFromApi(teamName).collect{ response ->
                when (response) {
                    is Resource.Success -> {
                        teamBadge[response.value.teams[0].idTeam] = response.value.teams[0].strTeamBadge.toString()
                    }
                }
            }
        }
    }

    fun getStandingByLeague(leagueId : String, season : String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getStandingByLeague(leagueId, season).collect{ response ->
                when (response) {
                    is Resource.Success -> {
                        Log.d("SAMBA5", "RESPONSE 22 : ${response.value.table}")
                        standings[leagueId] = response.value.table
                    }
                    is Resource.Loading -> {
                        Log.d("SAMBA5", "LOADING")
                    }
                    is Resource.Error -> {
                        Log.d("SAMBA5", "ERROR : ${response.error}")
                    }
                }
            }
        }
    }

    fun getFinalOrDateText(event : Event) : String {
        val offSet = getTimeZoneOffsetWithUTCInHours()
        var textToeventFinishOrTime = "Final"
        if (event.strStatus.equals("NS", ignoreCase = true) ||
            event.strStatus.equals("Not Started", ignoreCase = true) ||
            event.strStatus.isNullOrEmpty()) {
            textToeventFinishOrTime = LocalTime.parse(event.strTime).plusHours(offSet).toString()
        }
        if (event.strPostponed.equals("si", ignoreCase = true) ||
            event.strPostponed.equals("yes", ignoreCase = true) ||
            event.strPostponed.equals("y", ignoreCase = true)) {
            textToeventFinishOrTime = "Postponed"
        }
        return textToeventFinishOrTime
    }

}