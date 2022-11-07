package com.example.scoretracking.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.scoretracking.repository.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.internal.wait

fun<T,A> performGetResources(databaseQuery : () -> Flow<T>,
                             networkCall : suspend () -> Resource<A>,
                             saveCallResult : suspend (A) -> Unit) : Flow<Resource<T>> =
    flow<Resource<T>> {
        emit(Resource.Loading(true))
        val source : Flow<Resource<T>> =  databaseQuery.invoke().map {
            if (it != null) {
                Resource.Success(it)
            } else {
                Resource.Loading(true)
            }
        }
        emit(source.first())

        val responseStatus = networkCall.invoke()
        when(responseStatus) {
            is Resource.Success -> {
                saveCallResult(responseStatus.value)
                emit(source.first())
            }
            is Resource.Error -> {
                emit(Resource.Error("Error in the networkcall with $responseStatus"))
            }
            else -> {
                emit(Resource.Loading(true))
            }
        }
    }.flowOn(Dispatchers.IO)
