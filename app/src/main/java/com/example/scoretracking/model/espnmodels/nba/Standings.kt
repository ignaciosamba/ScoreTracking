package com.example.scoretracking.model.espnmodels.nba

data class Standings(
    val displayName: String,
    val entries: List<Entry>,
    val id: String,
    val name: String,
    val season: Int,
    val seasonType: Int
)