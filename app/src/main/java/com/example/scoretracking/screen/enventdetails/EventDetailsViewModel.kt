package com.example.scoretracking.screen.enventdetails

import com.example.scoretracking.model.services.LogInterface
import com.example.scoretracking.repository.leagues.LeagueRepository
import com.example.scoretracking.repository.main.GamesRepository
import com.example.scoretracking.screen.LoginBasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    logService: LogInterface,
    repository : GamesRepository
) : LoginBasicViewModel(logService){


}