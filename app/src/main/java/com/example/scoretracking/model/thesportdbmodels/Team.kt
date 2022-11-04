package com.example.scoretracking.model.thesportdbmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "teams")
data class Team(
    val idAPIfootball: String? = "",
    @ColumnInfo(name = "idLeague")
    val idLeague: String? = "",
    val idLeague2: String? = "",
    val idLeague3: String? = "",
    val idLeague4: String? = "",
    val idLeague5: String? = "",
    val idLeague6: String? = "",
    val idSoccerXML: String? = "",
    @PrimaryKey
    @ColumnInfo(name = "idTeam")
    val idTeam: String = "",
    val intFormedYear: String? = "",
    val intLoved: String? = "",
    val intStadiumCapacity: String? = "",
    val strAlternate: String? = "",
    val strCountry: String? = "",
    val strDescriptionDE: String? = "",
    val strDescriptionEN: String? = "",
    val strDescriptionES: String? = "",
    val strDescriptionFR: String? = "",
    val strDescriptionIT: String? = "",
    val strDescriptionJP: String? = "",
    val strDescriptionNO: String? = "",
    val strDescriptionPT: String? = "",
    val strDescriptionRU: String? = "",
    val strFacebook: String? = "",
    val strGender: String? = "",
    val strInstagram: String? = "",
    val strKeywords: String? = "",
    val strKitColour1: String? = "",
    val strKitColour2: String? = "",
    val strKitColour3: String? = "",
    val strLeague: String? = "",
    val strLeague2: String? = "",
    val strLeague3: String? = "",
    val strLeague4: String? = "",
    val strLeague5: String? = "",
    val strLeague6: String? = "",
    val strLeague7: String? = "",
    val strLocked: String? = "",
    val strManager: String? = "",
    val strRSS: String? = "",
    val strSport: String? = "",
    val strStadium: String? = "",
    val strStadiumDescription: String? = "",
    val strStadiumLocation: String? = "",
    val strStadiumThumb: String? = "",
    val strTeam: String? = "",
    val strTeamBadge: String? = "",
    val strTeamBanner: String? = "",
    val strTeamFanart1: String? = "",
    val strTeamFanart2: String? = "",
    val strTeamFanart3: String? = "",
    val strTeamFanart4: String? = "",
    val strTeamJersey: String? = "",
    val strTeamLogo: String? = "",
    val strTeamShort: String? = "",
    val strTwitter: String? = "",
    val strWebsite: String? = "",
    val strYoutube: String? = "",
    var isFavorite: Boolean = false
)