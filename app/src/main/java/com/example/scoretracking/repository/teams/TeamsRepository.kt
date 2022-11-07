package com.example.scoretracking.repository.teams

import com.example.scoretracking.data.dao.FavoriteLeagesDAO
import com.example.scoretracking.data.dao.FavoriteTeamsDAO
import com.example.scoretracking.data.dao.TeamsDAO
import com.example.scoretracking.model.localroommodels.LeagueFavorite
import com.example.scoretracking.model.localroommodels.TeamsFavorite
import com.example.scoretracking.model.thesportdbmodels.Team
import com.example.scoretracking.network.performGetResources
import com.example.scoretracking.repository.RemoteDataSourceTheSportDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class TeamsRepository  @Inject constructor(
    private val leaguesRemoteDataSource: RemoteDataSourceTheSportDB,
    private val leagueLocalDataSource : TeamsDAO,
    private val leagueFavoriteLocalDataSource : FavoriteTeamsDAO,
    private val leagueFavoriteLeagueDataSource: FavoriteLeagesDAO
) {

    fun getTeamsByLeagueId(leagueId : String) = performGetResources (
        databaseQuery = { leagueLocalDataSource.getAllTeamsByLeagueId(leagueId).distinctUntilChanged() },
        networkCall =  { leaguesRemoteDataSource.getTeamsByLeagueId(leagueId) },
        saveCallResult = { leagueLocalDataSource.instertAll(it.teams)})

    fun getFavoritesTeams() : Flow<List<TeamsFavorite>> {
        return leagueFavoriteLocalDataSource.getAllFavoriteTeams().distinctUntilChanged()
    }

    suspend fun saveLeagueIntoFavoritesDB (team : TeamsFavorite, isFavorite : Boolean) {
        if (isFavorite) {
            leagueFavoriteLocalDataSource.insertFavoriteTeam(team)
        } else {
            leagueFavoriteLocalDataSource.deleteFavoriteTeam(team)
        }
    }

    suspend fun saveTeamIntoDB(team : Team) {
        leagueLocalDataSource.instertTeam(team)
    }

    fun getFavoriteLeagues() : Flow<List<LeagueFavorite>>{
        return leagueFavoriteLeagueDataSource.getAllFavoriteLeagues().distinctUntilChanged()
    }

}