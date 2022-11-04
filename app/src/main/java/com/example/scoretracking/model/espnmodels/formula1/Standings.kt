package com.example.scoretracking.model.espnmodels.formula1

data class Standings(
    val displayName: String,
    val entries: List<Entry>,
    val id: String,
    val name: String,
    val season: String,
    val seasonType: Int
)