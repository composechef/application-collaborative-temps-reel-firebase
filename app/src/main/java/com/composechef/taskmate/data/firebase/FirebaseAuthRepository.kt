package com.composechef.taskmate.data.firebase

import com.composechef.taskmate.core.model.User
import com.composechef.taskmate.data.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject



class FirebaseAuthRepository @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.IdTokenListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.toUser())
        }
        auth.addIdTokenListener(listener)
        awaitClose { auth.removeIdTokenListener(listener) }
    }.distinctUntilChanged()

    override suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(name: String, email: String, password: String) {
        val user = auth.createUserWithEmailAndPassword(email, password).await().user!!
        user.updateProfile(userProfileChangeRequest { displayName = name }).await()
        user.getIdToken(true).await()
    }

    override suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override fun signOut() {
        auth.signOut()
    }
}

private fun FirebaseUser.toUser(): User = User(
    id = uid,
    name = displayName?.takeIf { it.isNotBlank() } ?: email.orEmpty().substringBefore('@'),
    email = email.orEmpty(),
)
