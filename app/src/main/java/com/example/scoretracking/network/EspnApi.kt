package com.example.scoretracking.network

import com.example.scoretracking.model.espnmodels.formula1.Formula1EspnStanding
import com.example.scoretracking.model.espnmodels.nba.NbaEspnStandingModel
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface EspnApi {

    @GET("racing/f1/standings")
    suspend fun getF1Standings(): Response<Formula1EspnStanding>

    @GET("basketball/nba/standings")
    suspend fun getNBAStandings(): Response<NbaEspnStandingModel>
}