package com.example.scoretracking.screen.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.commons.isValidEmail
import com.example.scoretracking.commons.isValidPassword
import com.example.scoretracking.commons.passwordMatches
import com.example.scoretracking.commons.snackbar.SnackBarManager
import com.example.scoretracking.model.login.RegisterUiState
import com.example.scoretracking.model.services.AccountInterface
import com.example.scoretracking.model.services.LogInterface
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.screen.LoginBasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.scoretracking.R.string as AppText


@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val accountInt: AccountInterface,
    private val loginInt : LogInterface
) : LoginBasicViewModel(loginInt) {

    var uiState = mutableStateOf(RegisterUiState())
        private set

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password
    private val repeatPassword get() = uiState.value.repeatPassword

    fun onEmailChange(email : String) {
        uiState.value = uiState.value.copy(email = email)
    }

    fun onPasswordChange(password : String) {
        uiState.value = uiState.value.copy(password = password)
    }

    fun onPasswordRepeatChange(passwordRepeated : String) {
        uiState.value = uiState.value.copy(repeatPassword = passwordRepeated)
    }

    fun onRegisterClicked(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackBarManager.showMessage(AppText.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackBarManager.showMessage(AppText.password_error)
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            SnackBarManager.showMessage(AppText.password_match_error)
            return
        }

        viewModelScope.launch(showErrorExceptionHandler) {
//            val createAccountTrace = Firebase.performance.newTrace(CREATE_ACCOUNT_TRACE)
//            createAccountTrace.start()
            accountInt.registerNewAccoun(email, password) { error ->
//                createAccountTrace.stop()
                if (error == null) {
                    openAndPopUp("${SportTrackerScreens.SelectFavoritesLeaguesScreen.name}/Login", SportTrackerScreens.RegisterScreen.name)
                } else {
                    onError(error)
                }
            }
        }
    }
}