package com.example.scoretracking.model.thesportdbmodels

data class LineupItem(val strPlayer: String = "",
                      val strTeam: String = "",
                      val idEvent: String = "",
                      val strEvent: String = "",
                      val intSquadNumber: String = "",
                      val idTeam: String = "",
                      val strFormation: String?,
                      val idPlayer: String = "",
                      val strPosition: String = "",
                      val strSubstitute: String = "",
                      val strCountry: String = "",
                      val strPositionShort: String?,
                      val strHome: String = "",
                      val idLineup: String = "",
                      val strSeason: String = "")