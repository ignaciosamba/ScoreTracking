package com.example.scoretracking.model.localroommodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritesLeagues")
data class LeagueFavorite(
    @PrimaryKey
    @ColumnInfo(name = "idLeague")
    val idLeague: String,
    @ColumnInfo(name = "strLeague")
    val strLeague: String,
    @ColumnInfo(name = "strLeagueAlternate")
    val strLeagueAlternate: String,
    @ColumnInfo(name = "strSport")
    val strSport: String
)