package com.example.scoretracking.model.espnmodels.nba

data class Team(
    val abbreviation: String,
    val displayName: String,
    val id: String,
    val isActive: Boolean,
    val location: String,
    val logos: List<Logo>,
    val name: String,
    val shortDisplayName: String,
    val uid: String
)