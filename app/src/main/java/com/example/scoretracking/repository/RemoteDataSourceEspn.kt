package com.example.scoretracking.repository

import com.example.scoretracking.network.BaseDataSourse
import com.example.scoretracking.network.EspnApi
import javax.inject.Inject

class RemoteDataSourceEspn @Inject constructor(
    private val espnApi: EspnApi
) : BaseDataSourse(){

    suspend fun getF1Standings() = getResults {
        espnApi.getF1Standings()
    }

    suspend fun getNBAStandings() = getResults {
        espnApi.getNBAStandings()
    }
}