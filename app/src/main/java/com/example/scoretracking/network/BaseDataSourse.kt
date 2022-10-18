package com.example.scoretracking.network

import android.util.Log
import com.example.scoretracking.repository.Resource
import retrofit2.Response

abstract class BaseDataSourse {

    protected suspend fun <T> getResults (call : suspend () -> Response<T>) : Resource<T> {
        try {
            Log.d("SAMBA66", "CALLING getResults")
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.Success(body) else Log.d("SAMBA66", "CALLING getResults ERROR")

            }
            return error("${response.code()} ${response.message()}")
        } catch (e : Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error (message : String) : Resource<T> {
        return Resource.Error("Network call has failed with exception: $message")
    }
}