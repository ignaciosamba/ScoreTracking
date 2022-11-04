package com.example.scoretracking.model.espnmodels.nba

data class Type(
    val abbreviation: String,
    val endDate: String,
    val hasStandings: Boolean,
    val id: String,
    val name: String,
    val startDate: String
)