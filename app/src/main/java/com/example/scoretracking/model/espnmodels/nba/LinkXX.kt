package com.example.scoretracking.model.espnmodels.nba

data class LinkXX(
    val href: String,
    val isExternal: Boolean,
    val isPremium: Boolean,
    val language: String,
    val rel: List<String>,
    val shortText: String,
    val text: String
)