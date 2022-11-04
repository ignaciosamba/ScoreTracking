package com.example.scoretracking.model.services

import com.example.scoretracking.model.firebasemodels.StorageLeague

interface StorageLeagueInterface {
    fun addLeagueListener(
        userId: String,
        onDocumentEvent: (Boolean, StorageLeague) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun removeLeagueListener()
    fun getFavoriteLeague(leagueId: String, onError: (Throwable) -> Unit, onSuccess: (StorageLeague) -> Unit)
    fun saveFavoriteLeague(league: StorageLeague, onResult: (Throwable?) -> Unit)
    fun updateFavoriteLeague(league: StorageLeague, onResult: (Throwable?) -> Unit)
    fun deleteFavoriteLeague(docPath: String, onResult: (Throwable?) -> Unit)
    fun deleteAllLeagueFavoritesForUser(userId: String, onResult: (Throwable?) -> Unit)
}