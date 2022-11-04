package com.example.scoretracking.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scoretracking.model.thesportdbmodels.Country
import kotlinx.coroutines.flow.Flow

/**
 * Country is the class whit all the information of one league.
 * The API service returns a list of "Country" but it's really a list of leagues.
 */

@Dao
interface LeagueDAO {

    @Query("SELECT * FROM leagues")
    fun getAllLeagues() : Flow<List<Country>>

    @Query("SELECT * FROM leagues WHERE strSport =:strSport")
    fun getAllLeaguesBySport(strSport : String) : Flow<List<Country>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun instertAll(leagues : List<Country>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLeagues(leagues : Country)
}