package com.example.scoretracking.model.espnmodels.nba

import com.example.scoretracking.model.espnmodels.nba.Standings

data class Children(
    val abbreviation: String,
    val id: String,
    val name: String,
    val standings: Standings,
    val uid: String
)