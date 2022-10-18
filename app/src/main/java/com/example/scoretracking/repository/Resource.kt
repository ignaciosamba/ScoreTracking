package com.example.scoretracking.repository


sealed class Resource<out T> {
    data class Loading<T>(val boolean: Boolean) : Resource<T>()
    data class Success<T>(val value : T) : Resource<T>()
    data class Error<T>(val error : String) : Resource<T>()
}