package com.salaheddine.vitaltrack.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class AppUser(val uid: String, val email: String)

object AuthService {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun currentUser(): AppUser? =
        auth.currentUser?.let { AppUser(it.uid, it.email ?: "") }

    suspend fun register(email: String, password: String): AppUser {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val user = authResult.user!!
        // mini profil Firestore
        db.collection("users").document(user.uid)
            .set(mapOf("email" to (user.email ?: ""), "createdAt" to System.currentTimeMillis()))
            .await()
        return AppUser(user.uid, user.email ?: "")
    }

    suspend fun login(email: String, password: String): AppUser {
        if (email.isBlank() || password.isBlank()) {
            throw IllegalArgumentException("Email et mot de passe ne peuvent pas être vides.")
        }

        auth.signInWithEmailAndPassword(email, password).await()
        val user = auth.currentUser!!
        return AppUser(user.uid, user.email ?: "")
    }

    fun logout() {
        auth.signOut()
    }
}