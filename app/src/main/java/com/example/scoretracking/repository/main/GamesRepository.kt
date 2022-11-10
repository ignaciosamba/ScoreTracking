package com.example.scoretracking.repository.main

import android.util.Log
import com.example.scoretracking.data.dao.LeagueDAO
import com.example.scoretracking.data.dao.TeamsDAO
import com.example.scoretracking.model.espnmodels.formula1.Formula1EspnStanding
import com.example.scoretracking.model.espnmodels.nba.NbaEspnStandingModel
import com.example.scoretracking.model.thesportdbmodels.GameEventsModel
import com.example.scoretracking.model.thesportdbmodels.LeagueStandingModel
import com.example.scoretracking.model.thesportdbmodels.TeamsModel
import com.example.scoretracking.repository.RemoteDataSourceEspn
import com.example.scoretracking.repository.RemoteDataSourceTheSportDB
import com.example.scoretracking.repository.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GamesRepository @Inject constructor(
    private val leaguesTheSportDBRemoteDataSource: RemoteDataSourceTheSportDB,
    private val leaguesEspnRemoteDataSource: RemoteDataSourceEspn,
    private val leagueLocalDataSource : TeamsDAO,
) {

    suspend fun getEventsByDay(date : String) : Flow<Resource<GameEventsModel>> =
        flow<Resource<GameEventsModel>> {
            emit(Resource.Loading(true))
            val response = leaguesTheSportDBRemoteDataSource.getEventsByDate(date)
            emit(response)
        }

    suspend fun getTeamBadgeFromApi(teamName : String) : Flow<Resource<TeamsModel>> =
        flow<Resource<TeamsModel>> {
            emit(Resource.Loading(true))
            val response = leaguesTheSportDBRemoteDataSource.getTeamsByName(teamName)
            emit(response)
        }

    fun getTeamBadge(idTeam : String) : Flow<String> {
        return leagueLocalDataSource.getTeamBadge(idTeam).distinctUntilChanged()
    }

    suspend fun getStandingByLeague(leagueId : String, season : String) : Flow<Resource<LeagueStandingModel>> =
        flow<Resource<LeagueStandingModel>> {
            emit(Resource.Loading(true))
            val response = leaguesTheSportDBRemoteDataSource.getStandingByLeague(leagueId, season)
            emit(response)
        }

    suspend fun getStandingNBAEspn() : Flow<Resource<NbaEspnStandingModel>> =
        flow<Resource<NbaEspnStandingModel>> {
            emit(Resource.Loading(true))
            val response = leaguesEspnRemoteDataSource.getNBAStandings()
            emit(response)
        }

    suspend fun getStandingF1Espn() : Flow<Resource<Formula1EspnStanding>> =
        flow<Resource<Formula1EspnStanding>> {
            emit(Resource.Loading(true))
            val response = leaguesEspnRemoteDataSource.getF1Standings()
            emit(response)
        }
}