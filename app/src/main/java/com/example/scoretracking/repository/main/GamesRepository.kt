package com.example.scoretracking.repository.main

import android.util.Log
import com.example.scoretracking.data.dao.TeamsDAO
import com.example.scoretracking.model.GameEventsModel
import com.example.scoretracking.model.Team
import com.example.scoretracking.model.TeamsModel
import com.example.scoretracking.repository.RemoteDataSource
import com.example.scoretracking.repository.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class GamesRepository @Inject constructor(
    private val leaguesRemoteDataSource: RemoteDataSource,
    private val leagueLocalDataSource : TeamsDAO,
) {

    suspend fun getEventsByDay(date : String) : Flow<Resource<GameEventsModel>> =
        flow<Resource<GameEventsModel>> {
            emit(Resource.Loading(true))
            val response = leaguesRemoteDataSource.getEventsByDate(date)
            emit(response)
        }

    suspend fun getTeamBadgeFromApi(teamName : String) : Flow<Resource<TeamsModel>> =
        flow<Resource<TeamsModel>> {
            emit(Resource.Loading(true))
            val response = leaguesRemoteDataSource.getTeamsByName(teamName)
            emit(response)
        }

    fun getTeamBadge(idTeam : String) : Flow<String> {
        return leagueLocalDataSource.getTeamBadge(idTeam).distinctUntilChanged()
    }
}