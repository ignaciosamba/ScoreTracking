package com.example.scoretracking.model.services

import com.example.scoretracking.model.firebasemodels.StorageTeam

interface StorageFavoriteTeamsInterface {
    fun addTeamsListener(
        userId: String,
        onDocumentEvent: (Boolean, StorageTeam) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun removeTeamsListener()
    fun getFavoriteTeam(teamId: String, onError: (Throwable) -> Unit, onSuccess: (StorageTeam) -> Unit)
    fun saveFavoriteTeam(team: StorageTeam, onResult: (Throwable?) -> Unit)
    fun updateFavoriteTeam(team: StorageTeam, onResult: (Throwable?) -> Unit)
    fun deleteFavoriteTeam(docPath: String, onResult: (Throwable?) -> Unit)
    fun deleteAllTeamsFavoritesForUser(userId: String, onResult: (Throwable?) -> Unit)
}