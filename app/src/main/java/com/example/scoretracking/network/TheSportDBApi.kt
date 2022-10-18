package com.example.scoretracking.network

import com.example.scoretracking.model.LeaguesModel
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

}