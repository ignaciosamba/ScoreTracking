package com.example.scoretracking.model.espnmodels.formula1

data class Type(
    val abbreviation: String,
    val endDate: String,
    val hasStandings: Boolean,
    val id: String,
    val name: String,
    val startDate: String
)