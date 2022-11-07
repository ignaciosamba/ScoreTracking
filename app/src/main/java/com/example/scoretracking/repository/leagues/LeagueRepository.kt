package com.example.scoretracking.repository.leagues

import androidx.lifecycle.distinctUntilChanged
import com.example.scoretracking.data.dao.FavoriteLeagesDAO
import com.example.scoretracking.data.dao.LeagueDAO
import com.example.scoretracking.data.dao.TeamsDAO
import com.example.scoretracking.model.localroommodels.LeagueFavorite
import com.example.scoretracking.network.performGetResources
import com.example.scoretracking.repository.RemoteDataSourceTheSportDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class LeagueRepository @Inject constructor(
    private val leaguesRemoteDataSource: RemoteDataSourceTheSportDB,
    private val leagueLocalDataSource : LeagueDAO,
    private val favoriteLeaguesLocalDataSource : FavoriteLeagesDAO,
    private val teamsLocalDataSource : TeamsDAO,
) {
    fun getLeaguesBySport(sportType : String) = performGetResources (
                databaseQuery = { leagueLocalDataSource.getAllLeaguesBySport(sportType).distinctUntilChanged() },
                networkCall =  { leaguesRemoteDataSource.getLeaguesBySport(sportType) },
                saveCallResult = { leagueLocalDataSource.instertAll(it.countries)})



    fun getFavoritesLeaguesBySport(league: String) : Flow<List<LeagueFavorite>> {
        return favoriteLeaguesLocalDataSource.getAllFavoriteLeaguesBySport(league).distinctUntilChanged()
    }

    suspend fun saveLeagueIntoFavoritesDB (league : LeagueFavorite, isFavorite : Boolean) {
        if (isFavorite) {
            favoriteLeaguesLocalDataSource.insertFavoriteLeague(league)
        } else {
            favoriteLeaguesLocalDataSource.deleteFavoriteLeague(league)
        }
    }

    fun saveTeamsByLeagueIntoFavoritesDB (leagueId : String) = performGetResources (
        databaseQuery = { teamsLocalDataSource.getAllTeamsByLeagueId(leagueId).distinctUntilChanged() },
        networkCall =  { leaguesRemoteDataSource.getTeamsByLeagueId(leagueId) },
        saveCallResult = { teamsLocalDataSource.instertAll(it.teams)})

}