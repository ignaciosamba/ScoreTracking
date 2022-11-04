package com.example.scoretracking.model.espnmodels.formula1

data class Stat(
    val abbreviation: String,
    val description: String,
    val displayName: String,
    val displayValue: String,
    val id: String,
    val name: String,
    val played: Boolean,
    val shortDisplayName: String,
    val shortName: String,
    val topFinish: Double,
    val type: String,
    val value: Double
)