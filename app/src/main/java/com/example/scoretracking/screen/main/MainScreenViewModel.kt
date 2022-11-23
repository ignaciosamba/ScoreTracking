package com.example.scoretracking.screen.main

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.commons.getTimeZoneOffsetWithUTCInHours
import com.example.scoretracking.model.StandingGenericModel
import com.example.scoretracking.model.espnmodels.formula1.Formula1EspnStanding
import com.example.scoretracking.model.espnmodels.nba.NbaEspnStandingModel
import com.example.scoretracking.model.thesportdbmodels.Event
import com.example.scoretracking.model.firebasemodels.StorageLeague
import com.example.scoretracking.model.firebasemodels.StorageTeam
import com.example.scoretracking.model.services.AccountInterface
import com.example.scoretracking.model.services.LogInterface
import com.example.scoretracking.model.services.StorageFavoriteTeamsInterface
import com.example.scoretracking.model.services.StorageLeagueInterface
import com.example.scoretracking.model.thesportdbmodels.LeagueStandingModel
import com.example.scoretracking.network.F1_LEAGUE_ID
import com.example.scoretracking.network.NBA_LEAGUE_ID
import com.example.scoretracking.repository.Resource
import com.example.scoretracking.repository.main.GamesRepository
import com.example.scoretracking.screen.LoginBasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    var events = mutableStateMapOf<String, ArrayList<Event>>()
        private set
    // This map is for all the events.
    var eventsFiltered = mutableStateMapOf<String, ArrayList<Event>>()
        private set

    // This map is for all the events.
    var eventsFiltered1 = mutableStateMapOf<String, ArrayList<Event>>()
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
    var standings = mutableStateMapOf<String, List<StandingGenericModel>>()
        private set

    private var job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

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
        var leagueHasEvents = false
        events.forEach { (_, eventList) ->
            eventList.forEach { event ->
                if (event.dateEvent == date.toString()) {
                    leagueHasEvents = true
                }

            }
        }
        if (!leagueHasEvents) {
            viewModelScope.launch(showErrorExceptionHandler) {
                repository.getEventsByDay(date.toString()).collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            favoriteLeaguesFromStorage.forEach { (idLeague, league) ->
                                val eventList = response.value.events.filter { event ->
                                    idLeague == event.idLeague && event.dateEvent == date.toString()
                                }

                                if (eventList.isNotEmpty()) {
                                    events[league.strLeague] = events[league.strLeague]?: ArrayList()
                                    events[league.strLeague]!!.addAll(
                                        eventList.sortedBy { it.strTimestamp })
                                    eventsFiltered[league.strLeague] = eventsFiltered[league.strLeague]?: ArrayList()
                                    eventsFiltered[league.strLeague]!!.addAll(
                                        eventList.sortedBy { it.strTimestamp })
                                }
                            }
                            favoriteTeamsFromStorage.forEach { (idTeam, team) ->
                                val eventList = response.value.events.filter { event ->
                                    (idTeam == event.idAwayTeam || idTeam == event.idHomeTeam) && event.dateEvent == date.toString()
                                }
                                if (eventList.isNotEmpty() && !events.containsKey(team.strLeague)) {
                                    events[eventList[0].strLeague?: ""] = events[eventList[0].strLeague?: ""]?: ArrayList()
                                    events[eventList[0].strLeague
                                        ?: ""]?.addAll(eventList.sortedBy { it.strTimestamp })
                                    eventsFiltered[eventList[0].strLeague?: ""] = eventsFiltered[eventList[0].strLeague?: ""]?: ArrayList()
                                    eventsFiltered[eventList[0].strLeague
                                        ?: ""]?.addAll(eventList.sortedBy { it.strTimestamp })
                                }
                            }
                        }
                        is Resource.Loading -> {
                            Log.d("getEventsByDate", "LOADING")
                        }
                        is Resource.Error -> {
                            onError(error = Throwable("${ response.error } getEventsByDate"))
                        }
                    }
                }
            }
        } else {
            events.forEach { (league, eventList) ->
                var eventListForDate = ArrayList<Event>()
                eventList.forEach { event ->
                    if (event.dateEvent == date.toString()) {
                        eventListForDate.add(event)
                    }
                }
                if (eventList.isNotEmpty()) {
                    eventsFiltered[league] = eventListForDate

                } else {
                    eventsFiltered.remove(league)
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
            if (leagueId.equals(NBA_LEAGUE_ID, ignoreCase = true)) {
                repository.getStandingNBAEspn().collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            standings[leagueId] = convertFromEspnNbaApiToGenericStanding(response.value)
                        }
                        is Resource.Loading -> {
                            Log.d("getStandingNBAEspn", "LOADING")
                        }
                        is Resource.Error -> {
                            onError(Throwable(response.error))
                        }
                    }
                }
            } else if (leagueId.equals(F1_LEAGUE_ID, ignoreCase = true)) {
            repository.getStandingF1Espn().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        standings[leagueId] = convertFromEspnF1ApiToGenericStanding(response.value)
                    }
                    is Resource.Loading -> {
                        Log.d("getStandingF1Espn", "LOADING")
                    }
                    is Resource.Error -> {
                        onError(Throwable(response.error))
                    }
                }
            }
        } else {
                repository.getStandingByLeague(leagueId, season).collect{ response ->
                    when (response) {
                        is Resource.Success -> {
                            standings[leagueId] = convertFromTheSportDBToGenericStanding(response.value)
                        }
                        is Resource.Loading -> {
                            Log.d("getStandingByLeague", "LOADING")
                        }
                        is Resource.Error -> {
                            onError(Throwable(response.error))
                        }
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
            textToeventFinishOrTime = if (!event.strStatus.isNullOrEmpty())
                LocalTime.parse(event.strTime).plusHours(offSet).toString() else  ""
        } else if (event.strPostponed.equals("si", ignoreCase = true) ||
            event.strPostponed.equals("yes", ignoreCase = true) ||
            event.strPostponed.equals("y", ignoreCase = true)) {
            textToeventFinishOrTime = "Postponed"
        } else if ((event.strStatus != null &&
            event.strStatus.toString().contains("H") ) ||
                    !event.strProgress.isNullOrEmpty()) {
            if ((event.strStatus.toString().equals("1H", ignoreCase = true) ||
                 event.strStatus.toString().equals("2H", ignoreCase = true)) &&
                 !event.strProgress.isNullOrEmpty()) {
                textToeventFinishOrTime = event.strProgress!!
            } else {
                textToeventFinishOrTime = (if (event.strStatus.equals("HT", ignoreCase = true)) {
                    "Half time"
                } else if(event.strStatus.toString().equals("1H", ignoreCase = true) ||
                    event.strStatus.toString().equals("2H", ignoreCase = true) &&
                    (LocalTime.now().isAfter(LocalTime.parse(event.strTime).plusHours(offSet)) &&
                            LocalTime.now().isBefore(LocalTime.parse(event.strTime)
                                .plusHours(offSet+1).plusMinutes(45))) &&
                        event.strProgress.isNullOrEmpty()) {
                    "Live"
                } else if (!event.strStatus.equals("NS", ignoreCase = true) ||
                    !event.strStatus.equals("Not Started", ignoreCase = true)
                ) {
                    "Final"
                } else {
                    LocalTime.parse(event.strTime).plusHours(offSet).toString()
                })
            }
        } else if ((event.strStatus.toString().equals("1H", ignoreCase = true) ||
                    event.strStatus.toString().equals("2H", ignoreCase = true))){
            textToeventFinishOrTime = "Live"
        }
        Log.d("SAMBA5", "NOW ${event.strHomeTeam} ${LocalTime.parse(event.strTime).plusHours(offSet+1)
            .plusMinutes(45)}")
        return textToeventFinishOrTime
    }

    fun convertFromTheSportDBToGenericStanding(leagueStandingModel: LeagueStandingModel): List<StandingGenericModel>{
        var standingGenericStanding = arrayListOf<StandingGenericModel>()
        leagueStandingModel.table.forEach {
            standingGenericStanding.add(StandingGenericModel (
                        position = it.intRank,
                        icon = it.strTeamBadge,
                        name = it.strTeam,
                        firstRow = it.intPlayed,
                        seconRow = it.intGoalDifference,
                        thirdRow = it.intPoints
                    ))
        }
        return standingGenericStanding
    }

    fun convertFromEspnNbaApiToGenericStanding(nbaEspnStandingModel: NbaEspnStandingModel): List<StandingGenericModel>{
        var standingGenericStanding = arrayListOf<StandingGenericModel>()
        nbaEspnStandingModel.children.forEach { conference ->
            val division = conference.name
            conference.standings.entries.forEach { team ->
                var standingGenericModel = StandingGenericModel("", "", "", "", "", "")
                standingGenericModel.name = team.team.name
                standingGenericModel.icon = team.team.logos[0].href
                standingGenericModel.division = division
                team.stats.forEach{ stat ->
                    if (stat.name.equals("divisionWinPercent", ignoreCase = true)) {
                        standingGenericModel.thirdRow = stat.displayValue
                    }
                    if (stat.name.equals("losses", ignoreCase = true)) {
                        standingGenericModel.seconRow = stat.displayValue
                    }
                    if (stat.name.equals("wins", ignoreCase = true)) {
                        standingGenericModel.firstRow = stat.displayValue
                    }
                    if (stat.name.equals("playoffSeed", ignoreCase = true)) {
                        standingGenericModel.position = stat.displayValue
                    }
                }
                standingGenericStanding.add(standingGenericModel)
            }
        }
        return standingGenericStanding
    }

    fun convertFromEspnF1ApiToGenericStanding(formula1EspnStanding: Formula1EspnStanding): List<StandingGenericModel>{
        var standingGenericStanding = arrayListOf<StandingGenericModel>()
        formula1EspnStanding.children.forEach { championship ->
            val division = championship.name
            championship.standings.entries.forEach { team ->
                var standingGenericModel = StandingGenericModel("", "", "", "", "", "")
                standingGenericModel.division = division
                if(division.equals("Driver Standings", ignoreCase = true)) {
                    standingGenericModel.name = team.athlete.name
                    standingGenericModel.icon = team.athlete.flag.href
                } else {
                    standingGenericModel.name = team.team.name
                }
                team.stats.forEach{ stat ->
                    if (stat.name.equals("championshipPts", ignoreCase = true) ||
                        stat.name.equals("points", ignoreCase = true)) {
                        standingGenericModel.thirdRow = stat.displayValue
                    }
                    if (stat.name.equals("rank", ignoreCase = true)) {
                        standingGenericModel.position = stat.displayValue
                    }
                }
                standingGenericStanding.add(standingGenericModel)
            }
        }
        return standingGenericStanding
    }

    fun getLivesEventsFromSport(sportType : String) {
        viewModelScope.launch(job) {
            repository.getEventLiveBySport(sportType).collect { gamesLiveEvents ->
                when (gamesLiveEvents) {
                    is Resource.Success -> {
                        if (!gamesLiveEvents.value.events.isNullOrEmpty())
                            covertLiveEvent(gamesLiveEvents.value.events)
                    }
                    is Resource.Error -> {
                        onError(Throwable("${gamesLiveEvents.error} getLivesEventsFromSport"))
                    }
                    is Resource.Loading -> {
                        Log.d("getStandingByLeague", "LOADING")
                    }
                }
            }
        }
    }

    fun deletePoller() {
        job.cancel()
        repository.deletePollerEventLiveBySport()
    }

    fun covertLiveEvent(gamesLiveEvents : List<Event>) {
        gamesLiveEvents.filter { event ->
            var foundEvent = false
            eventsFiltered.values.forEach { eventsList ->
                eventsList.forEach{
                    if (event.idEvent.equals(it.idEvent) &&
                        event.dateEvent == selectedDay.value.toString() &&
                        event.dateEvent == LocalDate.now().toString())
                        foundEvent = true
                }
            }
            foundEvent
        }

        gamesLiveEvents.forEach { liveEvents ->
            eventsFiltered.forEach { (league, eventList)->
                val listUpdated = ArrayList<Event>()
                eventList.forEach { event ->
                    if( event.idEvent == liveEvents.idEvent) {
                        event.intAwayScore = liveEvents.intAwayScore
                        event.intHomeScore = liveEvents.intHomeScore
                        event.strProgress = "${liveEvents.strProgress}'"
                    } else if (event.strStatus == "90") {
                        event.strStatus = "FT"
                    }
                    listUpdated.add(event)
                }
                eventsFiltered1[league] = listUpdated
            }
        }
        eventsFiltered1.forEach { (league, liveEventsList) ->
            eventsFiltered[league]?.clear()
            var listToAddPoi = eventsFiltered[league]?: ArrayList()
            liveEventsList.addAll(listToAddPoi)
            eventsFiltered[league] = liveEventsList
        }
        eventsFiltered1.clear()
    }


}