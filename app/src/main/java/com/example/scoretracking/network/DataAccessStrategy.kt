package com.example.scoretracking.network

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.scoretracking.repository.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

fun<T,A> performGetResources(databaseQuery : () -> Flow<T>,
                             networkCall : suspend () -> Resource<A>,
                             saveCallResult : suspend (A) -> Unit) : Flow<Resource<T>> =
    flow {
        Log.d("SAMBA8", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        emit(Resource.Loading(true))
        val source : Flow<Resource<T>> =  databaseQuery.invoke().map {
            Log.d("SAMBA", "EL IT ES: ${it.toString()}")
            if (it != null) {
                Resource.Success(it)
            } else {
                Resource.Loading(true)
            }
        }.distinctUntilChanged()
        Log.d("SAMBA8", "EMITING source :  ${source.first()}")
        emit(source.first())

        val responseStatus = networkCall.invoke()
        when(responseStatus) {
            is Resource.Success -> {
                Log.d("SAMBA8", "CALLING SAVERESULTS")
                saveCallResult(responseStatus.value)
                Log.d("SAMBA8", "EMITING SAVE CALLING SAVERESULTS")
            }
            is Resource.Error -> {
                emit(Resource.Error("Error in the networkcall with ${responseStatus}"))
            }
            else -> {
                emit(Resource.Loading(true))
            }
        }

    }
