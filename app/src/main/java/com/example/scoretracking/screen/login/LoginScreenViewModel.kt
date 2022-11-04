package com.example.scoretracking.screen.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.commons.isValidEmail
import com.example.scoretracking.commons.snackbar.SnackBarManager
import com.example.scoretracking.model.login.LoginUiState
import com.example.scoretracking.model.services.AccountInterface
import com.example.scoretracking.model.services.LogInterface
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.screen.LoginBasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.scoretracking.R.string as AppText

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val accountInt: AccountInterface,
    private val loginInt : LogInterface
): LoginBasicViewModel(loginInt){

    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password

    fun onEmailChange(email : String) {
        uiState.value = uiState.value.copy(email = email)
    }

    fun onPasswordChange(password : String) {
        uiState.value = uiState.value.copy(password = password)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackBarManager.showMessage(AppText.email_error)
            return
        }

        if (password.isBlank()) {
            SnackBarManager.showMessage(AppText.empty_password_error)
            return
        }

        viewModelScope.launch(showErrorExceptionHandler) {
            accountInt.authenticate(email, password) { error ->
                if (error == null) {
                    openAndPopUp(SportTrackerScreens.SelectFavoritesLeaguesScreen.name, SportTrackerScreens.LoginScreen.name)
                } else {
                    onError(error)
                }
            }
        }
    }


    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackBarManager.showMessage(AppText.email_error)
            return
        }

        viewModelScope.launch(showErrorExceptionHandler) {
            accountInt.sendRecoveryEmail(email) { error ->
                if (error != null) onError(error)
                else SnackBarManager.showMessage(AppText.recovery_email_sent)
            }
        }
    }

    fun onCreateAccountClicked(openScreen: (String) -> Unit) {
        openScreen(SportTrackerScreens.RegisterScreen.name)
    }


}