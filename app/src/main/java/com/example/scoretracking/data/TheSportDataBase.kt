package com.example.scoretracking.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scoretracking.data.dao.FavoriteLeagesDAO
import com.example.scoretracking.data.dao.FavoriteTeamsDAO
import com.example.scoretracking.data.dao.LeagueDAO
import com.example.scoretracking.data.dao.TeamsDAO
import com.example.scoretracking.model.thesportdbmodels.Country
import com.example.scoretracking.model.localroommodels.LeagueFavorite
import com.example.scoretracking.model.thesportdbmodels.Team
import com.example.scoretracking.model.localroommodels.TeamsFavorite


@Database(entities = [Country::class, LeagueFavorite::class, Team::class, TeamsFavorite::class], version = 1, exportSchema = false)
abstract class TheSportDataBase : RoomDatabase(){
    abstract fun leaguesDAO() : LeagueDAO
    abstract fun favoriteLeaguesDao() : FavoriteLeagesDAO
    abstract fun teamsDao() : TeamsDAO
    abstract fun favoriteTeamsDao() : FavoriteTeamsDAO
}