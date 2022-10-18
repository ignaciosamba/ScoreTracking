package com.example.scoretracking.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scoretracking.data.dao.FavoriteLeagesDAO
import com.example.scoretracking.data.dao.LeagueDAO
import com.example.scoretracking.model.Country
import com.example.scoretracking.model.LeagueFavorite


@Database(entities = [Country::class, LeagueFavorite::class], version = 1, exportSchema = false)
abstract class TheSportDataBase : RoomDatabase(){
    abstract fun leaguesDAO() : LeagueDAO
    abstract fun favoriteLeaguesDao() : FavoriteLeagesDAO
}