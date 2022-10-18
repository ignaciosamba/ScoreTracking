package com.example.scoretracking.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "leagues")
data class Country(
    @ColumnInfo(name = "dateFirstEvent")
    val dateFirstEvent: String?,
//    @ColumnInfo(name = "idAPIfootball")
    val idAPIfootball: String?,
    @ColumnInfo(name = "idCup")
    val idCup: String?,
    @PrimaryKey
    @ColumnInfo(name = "idLeague")
    val idLeague: String,
//    @ColumnInfo(name = "intDivision")
//    val intDivision: String?,
//    @ColumnInfo(name = "intFormedYear")
//    val intFormedYear: String?,
    @ColumnInfo(name = "strBadge")
    val strBadge: String?,
//    @ColumnInfo(name = "strBanner")
//    val strBanner: String?,
//    @ColumnInfo(name = "strComplete")
//    val strComplete: String?,
    @ColumnInfo(name = "strCountry")
    val strCountry: String?,
//    @ColumnInfo(name = "strCurrentSeason")
//    val strCurrentSeason: String?,
//    @ColumnInfo(name = "strDescriptionEN")
//    val strDescriptionEN: String?,
//    @ColumnInfo(name = "strDescriptionES")
//    val strDescriptionES: String?,
//    @ColumnInfo(name = "strDescriptionFR")
//    val strDescriptionFR: String?,
//    @ColumnInfo(name = "strDescriptionIT")
//    val strDescriptionIT: String?,
//    @ColumnInfo(name = "strFacebook")
//    val strFacebook: String?,
//    @ColumnInfo(name = "strFanart1")
//    val strFanart1: String?,
//    @ColumnInfo(name = "strGender")
//    val strGender: String?,
//    @ColumnInfo(name = "strInstagram")
//    val strInstagram: String?,
    @ColumnInfo(name = "strLeague")
    val strLeague: String?,
    @ColumnInfo(name = "strLeagueAlternate")
    val strLeagueAlternate: String?,
//    @ColumnInfo(name = "strLocked")
//    val strLocked: String?,
//    @ColumnInfo(name = "strLogo")
//    val strLogo: String?,
//    @ColumnInfo(name = "strNaming")
//    val strNaming: String?,
//    @ColumnInfo(name = "strPoster")
//    val strPoster: String?,
//    @ColumnInfo(name = "strRSS")
//    val strRSS: String?,
    @ColumnInfo(name = "strSport")
    val strSport: String?,
//    @ColumnInfo(name = "strTrophy")
//    val strTrophy: String?,
//    @ColumnInfo(name = "strTvRights")
//    val strTvRights: String?,
//    @ColumnInfo(name = "strTwitter")
//    val strTwitter: String?,
//    @ColumnInfo(name = "strWebsite")
//    val strWebsite: String?,
//    @ColumnInfo(name = "strYoutube")
//    val strYoutube: String?,
    var isFavorite : Boolean

)