package com.example.scoretracking.model.services

import com.example.scoretracking.model.firebasemodels.StorageLeague
import com.example.scoretracking.model.firebasemodels.StorageTeam
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class StorageFavoriteImpl @Inject constructor()
    : StorageFavoriteTeamsInterface, StorageLeagueInterface {
    private var listenerRegistration: ListenerRegistration? = null

//-------------------------------------------------LEAGUES------------------------------------------

    override fun addLeagueListener(
        userId: String,
        onDocumentEvent: (Boolean, StorageLeague) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val query = Firebase.firestore.collection(LEAGUE_COLLECTION).whereEqualTo(USER_ID, userId)

        listenerRegistration = query.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            value?.documentChanges?.forEach {
                val wasDocumentDeleted = it.type == DocumentChange.Type.REMOVED
                val task = it.document.toObject<StorageLeague>().copy(idLeague = it.document.toObject<StorageLeague>().idLeague)
                onDocumentEvent(wasDocumentDeleted, task)
            }
        }
    }

    override fun addLeagueListenerCompleted(
        userId: String,
        onDocumentEvent: (Boolean, StorageLeague) -> Unit,
        onError: (Throwable) -> Unit,
        onComlete: () -> Unit
    ) {
        val query = Firebase.firestore.collection(LEAGUE_COLLECTION).whereEqualTo(USER_ID, userId)

        listenerRegistration = query.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            value?.documentChanges?.forEach {
                val wasDocumentDeleted = it.type == DocumentChange.Type.REMOVED
                val task = it.document.toObject<StorageLeague>().copy(idLeague = it.document.toObject<StorageLeague>().idLeague)
                onDocumentEvent(wasDocumentDeleted, task)
            }
            onComlete()
        }
    }

    override fun removeLeagueListener() {
        listenerRegistration?.remove()
    }

    override fun getFavoriteLeague(
        leagueId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (StorageLeague) -> Unit
    ) {
        Firebase.firestore
            .collection(LEAGUE_COLLECTION)
            .document(leagueId)
            .get()
            .addOnFailureListener { error -> onError(error) }
            .addOnSuccessListener { result ->
                val task = result.toObject<StorageLeague>()?.copy(idLeague = result.toObject<StorageLeague>()!!.idLeague)
                onSuccess(task ?: StorageLeague())
            }
    }

    override fun saveFavoriteLeague(league: StorageLeague, onResult: (Throwable?) -> Unit) {
        val docPath = league.idLeague.plus(league.userId)
        Firebase.firestore
            .collection(LEAGUE_COLLECTION)
            .document(docPath)
            .set(league)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun updateFavoriteLeague(league: StorageLeague, onResult: (Throwable?) -> Unit) {
        val docPath = league.idLeague.plus(league.userId)
        Firebase.firestore
            .collection(LEAGUE_COLLECTION)
            .document(docPath)
            .set(league)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteFavoriteLeague(docPath: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(LEAGUE_COLLECTION)
            .document(docPath)
            .delete()
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteAllLeagueFavoritesForUser(userId: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(LEAGUE_COLLECTION)
            .whereEqualTo(USER_ID, userId)
            .get()
            .addOnFailureListener { error -> onResult(error) }
            .addOnSuccessListener { result ->
                for (document in result) document.reference.delete()
                onResult(null)
            }
    }


//--------------------------------------------------TEAMS-------------------------------------------

    override fun addTeamsListener(
        userId: String,
        onDocumentEvent: (Boolean, StorageTeam) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val query = Firebase.firestore.collection(TEAM_COLLECTION).whereEqualTo(USER_ID, userId)

        listenerRegistration = query.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            value?.documentChanges?.forEach {
                val wasDocumentDeleted = it.type == DocumentChange.Type.REMOVED
                val task = it.document.toObject<StorageTeam>().copy(idTeam = it.document.toObject<StorageTeam>().idTeam)
                onDocumentEvent(wasDocumentDeleted, task)
            }
        }
    }

    override fun removeTeamsListener() {
        listenerRegistration?.remove()
    }

    override fun getFavoriteTeam(
        teamId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (StorageTeam) -> Unit
    ) {
        Firebase.firestore
            .collection(TEAM_COLLECTION)
            .document(teamId)
            .get()
            .addOnFailureListener { error -> onError(error) }
            .addOnSuccessListener { result ->
                val task = result.toObject<StorageTeam>()?.copy(idTeam = result.toObject<StorageTeam>()!!.idTeam)
                onSuccess(task ?: StorageTeam())
            }
    }

    override fun saveFavoriteTeam(team: StorageTeam, onResult: (Throwable?) -> Unit) {
        val docPath = team.idTeam.plus(team.userId)
        Firebase.firestore
            .collection(TEAM_COLLECTION)
            .document(docPath)
            .set(team)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun updateFavoriteTeam(team: StorageTeam, onResult: (Throwable?) -> Unit) {
        val docPath = team.idTeam.plus(team.userId)
        Firebase.firestore
            .collection(TEAM_COLLECTION)
            .document(docPath)
            .set(team)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteFavoriteTeam(docPath: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(TEAM_COLLECTION)
            .document(docPath)
            .delete()
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteAllTeamsFavoritesForUser(userId: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(TEAM_COLLECTION)
            .whereEqualTo(USER_ID, userId)
            .get()
            .addOnFailureListener { error -> onResult(error) }
            .addOnSuccessListener { result ->
                for (document in result) document.reference.delete()
                onResult(null)
            }
    }


    companion object {
        private const val TEAM_COLLECTION = "FavoriteTeams"
        private const val LEAGUE_COLLECTION = "FavoriteLeagues"
        private const val USER_ID = "userId"
    }
}