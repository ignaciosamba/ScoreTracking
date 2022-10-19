package com.example.scoretracking.data.dao

import androidx.room.*
import com.example.scoretracking.model.Country
import com.example.scoretracking.model.LeagueFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteLeagesDAO {

    @Query("SELECT * FROM favoritesLeagues")
    fun getAllFavoriteLeagues() : Flow<List<LeagueFavorite>>

    @Query("SELECT * FROM favoritesLeagues WHERE strSport =:strSport")
    fun getAllFavoriteLeaguesBySport(strSport : String) : Flow<List<LeagueFavorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun instertAll(leagues : List<LeagueFavorite>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteLeague(leagues : LeagueFavorite)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavoriteLeagues(leagues : LeagueFavorite)

    @Delete
    suspend fun deleteFavoriteLeague(league: LeagueFavorite)
}