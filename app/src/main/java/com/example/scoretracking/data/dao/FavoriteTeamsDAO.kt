package com.example.scoretracking.data.dao

import androidx.room.*
import com.example.scoretracking.model.localroommodels.TeamsFavorite
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteTeamsDAO {

    @Query("SELECT * FROM favoritesTeams")
    fun getAllFavoriteTeams() : Flow<List<TeamsFavorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTeam(team : TeamsFavorite)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavoriteTeam(team : TeamsFavorite)

    @Delete
    suspend fun deleteFavoriteTeam(team : TeamsFavorite)
}