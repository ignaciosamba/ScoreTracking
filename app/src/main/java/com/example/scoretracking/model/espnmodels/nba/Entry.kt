package com.example.scoretracking.model.espnmodels.nba

import com.example.scoretracking.model.espnmodels.nba.Stat
import com.example.scoretracking.model.espnmodels.nba.Team

data class Entry(
    val stats: List<Stat>,
    val team: Team
)