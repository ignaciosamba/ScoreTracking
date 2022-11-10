package com.example.scoretracking.screen.settings

import androidx.lifecycle.viewModelScope
import com.example.scoretracking.model.services.AccountInterface
import com.example.scoretracking.model.services.LogInterface
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.screen.LoginBasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogInterface,
    private val accountService: AccountInterface
    ) : LoginBasicViewModel(logService) {

    fun logOut(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            accountService.signOut()
            openAndPopUp(SportTrackerScreens.LoginScreen.name, SportTrackerScreens.GamesScreen.name)
        }
    }
}