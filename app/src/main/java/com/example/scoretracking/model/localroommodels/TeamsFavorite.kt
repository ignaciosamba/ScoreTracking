package com.example.scoretracking.model.localroommodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritesTeams")
data class TeamsFavorite (
    @PrimaryKey
    @ColumnInfo(name = "idTeam")
    val idTeam: String,
    @ColumnInfo(name = "strTeam")
    val strTeam: String,
    @ColumnInfo(name = "strTeamLogo")
    val strTeamLogo: String,
    @ColumnInfo(name = "strLeague")
    val strLeague: String
)