package com.example.scoretracking.repository.leagues

import android.util.Log
import com.example.scoretracking.data.dao.FavoriteLeagesDAO
import com.example.scoretracking.data.dao.LeagueDAO
import com.example.scoretracking.model.Country
import com.example.scoretracking.model.LeagueFavorite
import com.example.scoretracking.model.LeaguesModel
import com.example.scoretracking.network.performGetResources
import com.example.scoretracking.repository.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class LeagueRepository @Inject constructor(
    private val leaguesRemoteDataSource: LeaguesRemoteDataSource,
    private val leagueLocalDataSource : LeagueDAO,
    private val favoriteLeaguesLocalDataSource : FavoriteLeagesDAO
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
            Log.d("SAMBA22", "INSERT VALUE WITH $isFavorite")
            favoriteLeaguesLocalDataSource.insertFavoriteLeague(league)
        } else {
            Log.d("SAMBA22", "DELETE VALUE WITH $isFavorite")
            favoriteLeaguesLocalDataSource.deleteFavoriteLeague(league)
        }
    }

}