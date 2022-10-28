package com.example.scoretracking.screen.main

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.commons.getTimeZoneOffsetWithUTCInHours
import com.example.scoretracking.model.Event
import com.example.scoretracking.model.StorageLeague
import com.example.scoretracking.model.service.AccountInterface
import com.example.scoretracking.model.service.LogInterface
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
class GameScreenViewModel @Inject constructor(
    private val repository: GamesRepository,
    logService: LogInterface,
    private val storageLeagueService: StorageLeagueInterface,
    private val accountService: AccountInterface
) : LoginBasicViewModel(logService) {

    // This map is for all the events.
    var events = mutableStateMapOf<String, List<Event>>()
        private set
    // This map is for all the teams icons.
    var teamBadge = mutableStateMapOf<String, String>()
        private set
    // This list is for all the favorite leagues
    private var favoriteLeaguesFromStorage = mutableStateMapOf<String, StorageLeague>()

    var selectedDay = mutableStateOf<LocalDate>(LocalDate.now())
        private set

    init {
        Log.d("SAMBA3", "LALALLALALALAL")
        getEventsByDate()
    }

    fun addListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageLeagueService.addLeagueListener(accountService.getUserId(), ::onDocumentLeagueEvent, ::onError)
        }
    }

    fun removeListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageLeagueService.removeLeagueListener()
        }
    }

    fun getEventsByDate(date : LocalDate = LocalDate.now()) {
        Log.d("SAMBA", "OFFSET: ${LocalTime.now()}")
        viewModelScope.launch(showErrorExceptionHandler) {
            repository.getEventsByDay(date.toString()).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        favoriteLeaguesFromStorage.forEach { (idLeague, league) ->
                            val eventList = response.value.events.filter { event ->
                                idLeague == event.idLeague && event.dateEvent == date.toString()
                            }
                            if (eventList.isNotEmpty()) events[league.strLeague] = eventList
                        }
                    }
                    is Resource.Loading -> {
                        Log.d("SAMBA", "LOADING")
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

    fun getTeamBadge(idTeam : String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTeamBadge(idTeam?:"").collect(){
                teamBadge[idTeam?:""] = it
            }
        }
    }

    fun getFinalOrDateText(event : Event) : String {
        val offSet = getTimeZoneOffsetWithUTCInHours()
        var textToeventFinishOrTime = "Final"
        Log.d("SAMBA1", "STATUS : ${event.strStatus}")
        if (event.strStatus.equals("NS", ignoreCase = true) ||
            event.strStatus.equals("Not Started", ignoreCase = true) ||
            event.strStatus.isNullOrEmpty()) {
            textToeventFinishOrTime = LocalTime.parse(event.strTime).plusHours(offSet).toString()
        }
        return textToeventFinishOrTime
    }

}