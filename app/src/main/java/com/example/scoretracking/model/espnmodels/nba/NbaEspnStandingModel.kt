package com.example.scoretracking.model.espnmodels.nba

import com.example.scoretracking.model.espnmodels.nba.Children
import com.example.scoretracking.model.espnmodels.nba.LinkXX
import com.example.scoretracking.model.espnmodels.nba.Season

data class NbaEspnStandingModel(
    val abbreviation: String,
    val children: List<Children>,
    val id: String,
    val name: String,
    val shortName: String,
    val uid: String
)