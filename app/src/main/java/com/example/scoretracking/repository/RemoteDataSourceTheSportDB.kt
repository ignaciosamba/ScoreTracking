package com.example.scoretracking.repository

import com.example.scoretracking.network.BaseDataSourse
import com.example.scoretracking.network.TheSportDBApi
import javax.inject.Inject

class RemoteDataSourceTheSportDB @Inject constructor(
    private val sportDBApi: TheSportDBApi) : BaseDataSourse(){

    suspend fun getLeaguesBySport(sportType : String) = getResults {
        sportDBApi.getLeaguesBySport(sportType)
    }

    suspend fun getTeamsByLeagueId(leagueId : String) = getResults {
        sportDBApi.getTeamsByLeagueId(leagueId)
    }

    suspend fun getTeamsByLeagueName(leagueName : String) = getResults {
        sportDBApi.getTeamsByLeagueName(leagueName)
    }


    suspend fun getEventsByDate(date : String) = getResults {
        sportDBApi.getEventsByDate(date)
    }

    suspend fun getTeamsByName(teamName : String) = getResults {
        sportDBApi.getTeamsByName(teamName)
    }

    suspend fun getStandingByLeague(leagueId : String, season : String) = getResults {
        sportDBApi.getStandingByLeague(leagueId, season)
    }
}