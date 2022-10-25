package com.example.scoretracking.model.service

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class AccountIml @Inject constructor() : AccountInterface {

    override fun hasUser(): Boolean {
        Log.d("SAMBA", "HAAAAAAAAAAAAAAAAAS")
        var value = false
        try {
            value = Firebase.auth.currentUser != null
        } catch (e : Exception) {
            Log.d("SAMBA", "ERROR: ${e.stackTraceToString()}")
        }
        return value
    }

    override fun getUserId(): String {
        return Firebase.auth.currentUser?.uid.orEmpty()
    }

    override fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun sendRecoveryEmail(email: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun registerNewAccoun(email: String, password: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d("SAMBA", "${it.exception?.stackTraceToString()}")
                onResult(it.exception)
            }
    }

    override fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit) {
        val credential = EmailAuthProvider.getCredential(email, password)

        Firebase.auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener {
                Log.d("SAMBA", "${it.exception?.stackTraceToString()}")
                onResult(it.exception)
            }
    }

    override fun deleteAccount(onResult: (Throwable?) -> Unit) {
        Firebase.auth.currentUser!!.delete()
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }
}