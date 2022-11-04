package com.example.scoretracking.model.espnmodels.nba

data class Stat(
    val abbreviation: String,
    val description: String,
    val displayName: String,
    val displayValue: String,
    val id: String,
    val name: String,
    val shortDisplayName: String,
    val summary: String,
    val type: String,
    val value: Double
)