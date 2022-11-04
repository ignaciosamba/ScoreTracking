package com.example.scoretracking.model.services


interface LogInterface {
    fun logNonFatalCrash(throwable: Throwable?)
}