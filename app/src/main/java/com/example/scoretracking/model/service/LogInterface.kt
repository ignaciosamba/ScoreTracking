package com.example.scoretracking.model.service


interface LogInterface {
    fun logNonFatalCrash(throwable: Throwable?)
}