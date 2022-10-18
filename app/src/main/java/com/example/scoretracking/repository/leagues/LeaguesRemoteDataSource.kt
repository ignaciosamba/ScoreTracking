package com.example.scoretracking.repository.leagues

import android.util.Log
import com.example.scoretracking.network.BaseDataSourse
import com.example.scoretracking.network.TheSportDBApi
import javax.inject.Inject

class LeaguesRemoteDataSource @Inject constructor(
    private val sportDBApi: TheSportDBApi) : BaseDataSourse(){

    suspend fun getLeaguesBySport(sportType : String) = getResults {
        Log.d("SAMBA1", "CALLING getLeaguesBySporttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt")
        sportDBApi.getLeaguesBySport(sportType)
    }
}