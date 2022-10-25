package com.example.scoretracking.screen

import androidx.lifecycle.ViewModel
import com.example.scoretracking.commons.snackbar.SnackBarManager
import com.example.scoretracking.commons.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.example.scoretracking.model.service.LogInterface
import kotlinx.coroutines.CoroutineExceptionHandler

open class LoginBasicViewModel (private val logService: LogInterface) : ViewModel() {

    open val showErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    open val logErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logService.logNonFatalCrash(throwable)
    }

    open fun onError(error: Throwable) {
        SnackBarManager.showMessage(error.toSnackbarMessage())
        logService.logNonFatalCrash(error)
    }
}