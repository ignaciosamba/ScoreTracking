package com.example.scoretracking.model.services

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class LogImpl @Inject constructor() : LogInterface {
    override fun logNonFatalCrash(throwable: Throwable?) {
        if (throwable != null) Firebase.crashlytics.recordException(throwable)
    }
}