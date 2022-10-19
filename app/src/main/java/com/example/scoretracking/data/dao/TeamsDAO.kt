package com.example.scoretracking.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scoretracking.model.Team
import kotlinx.coroutines.flow.Flow


@Dao
interface TeamsDAO {
//    @Query("SELECT * FROM teams")
//    fun getAllTeams() : Flow<List<Team>>

    @Query("SELECT * FROM teams WHERE idLeague =:idLeague")
    fun getAllTeamsByLeagueId(idLeague : String) : Flow<List<Team>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun instertAll(teams : List<Team>)
}