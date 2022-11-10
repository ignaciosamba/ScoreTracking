package com.example.scoretracking.screen.login

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scoretracking.commons.isValidEmail
import com.example.scoretracking.commons.snackbar.SnackBarManager
import com.example.scoretracking.model.firebasemodels.StorageLeague
import com.example.scoretracking.model.firebasemodels.StorageTeam
import com.example.scoretracking.model.localroommodels.TeamsFavorite
import com.example.scoretracking.model.login.LoginUiState
import com.example.scoretracking.model.services.AccountInterface
import com.example.scoretracking.model.services.LogInterface
import com.example.scoretracking.model.services.StorageFavoriteTeamsInterface
import com.example.scoretracking.model.services.StorageLeagueInterface
import com.example.scoretracking.model.thesportdbmodels.Country
import com.example.scoretracking.model.thesportdbmodels.Team
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.repository.Resource
import com.example.scoretracking.repository.teams.TeamsRepository
import com.example.scoretracking.screen.LoginBasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.scoretracking.R.string as AppText

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val accountInt: AccountInterface,
    private val loginInt : LogInterface,
    private val repository: TeamsRepository,
    private val storageLeagueInterface: StorageLeagueInterface,
    private val accountService: AccountInterface
): LoginBasicViewModel(loginInt){

    var clicked = mutableStateOf(false)
        private set

    var uiState = mutableStateOf(LoginUiState())
        private set

    var lmbd: ((String, String) -> Unit)? = null

    // This list is for all the favorite leagues
    private var favoriteLeaguesFromStorage = mutableStateMapOf<String, StorageLeague>()

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password


    fun addListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageLeagueInterface.addLeagueListenerCompleted(accountService.getUserId(), ::onDocumentLeagueEvent, ::onError, ::onComlete)
        }
    }

    fun removeListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageLeagueInterface.removeLeagueListener()
        }
    }

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
            clicked.value = true
            accountInt.authenticate(email, password) { error ->
                if (error == null) {
                    lmbd = openAndPopUp
                    addListener()
                } else {
                    clicked.value = false
                    onError(error)
                }
            }
        }
    }

    private fun getFavoritesTeamsToRoom() {
        viewModelScope.launch {
            favoriteLeaguesFromStorage.map {
                async { repository.getTeamsByLeagueId(it.value.idLeague).collect() { league ->
                    when(league) {
                        is Resource.Success -> Log.d("getTeamsByLeagueId", "league ${it.value.idLeague} size ${league.value.size}")
                    }
                } }.await()
            }
            if(favoriteLeaguesFromStorage.isEmpty()) {
                lmbd?.let { it("${SportTrackerScreens.SelectFavoritesLeaguesScreen.name}/login", SportTrackerScreens.LoginScreen.name) }
            } else {
                lmbd?.let { it(SportTrackerScreens.GamesScreen.name, SportTrackerScreens.LoginScreen.name) }
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

    private fun onDocumentLeagueEvent(wasDocumentDeleted: Boolean, league : StorageLeague) {
        if (wasDocumentDeleted) {
            favoriteLeaguesFromStorage.remove(league.idLeague)
        } else {
            favoriteLeaguesFromStorage[league.idLeague] = league
        }
    }

    private fun onComlete() {
        getFavoritesTeamsToRoom()
    }

}