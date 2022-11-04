package com.example.scoretracking.network

import com.example.scoretracking.model.thesportdbmodels.GameEventsModel
import com.example.scoretracking.model.thesportdbmodels.LeagueStandingModel
import com.example.scoretracking.model.thesportdbmodels.LeaguesModel
import com.example.scoretracking.model.thesportdbmodels.TeamsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface TheSportDBApi {

//    @GET("v1/json/$API_KEY/all_leagues.php")
//    suspend fun getAllLeagues() : LeaguesModel

    @GET("v1/json/$API_KEY/search_all_leagues.php")
    suspend fun getLeaguesBySport(@Query("s") sport : String?): Response<LeaguesModel>

    @GET("v1/json/$API_KEY/lookup_all_teams.php")
    suspend fun getTeamsByLeagueId(@Query("id") leagueId : String?): Response<TeamsModel>

    @GET("v1/json/$API_KEY/eventsday.php")
    suspend fun getEventsByDate(@Query("d") date : String?): Response<GameEventsModel>

    @GET("v1/json/$API_KEY/searchteams.php")
    suspend fun getTeamsByName(@Query("t") teamName : String?): Response<TeamsModel>

    @GET("v1/json/$API_KEY/lookuptable.php")
    suspend fun getStandingByLeague(@Query("l") leagueId: String?, @Query("s") season: String): Response<LeagueStandingModel>

}