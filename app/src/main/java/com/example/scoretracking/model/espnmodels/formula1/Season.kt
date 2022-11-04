package com.example.scoretracking.model.espnmodels.formula1

data class Season(
    val displayName: String,
    val endDate: String,
    val startDate: String,
    val types: List<Type>,
    val year: Int
)