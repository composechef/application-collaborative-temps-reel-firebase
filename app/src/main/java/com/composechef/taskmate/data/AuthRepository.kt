package com.composechef.taskmate.data

import com.composechef.taskmate.core.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>

    suspend fun signIn(email: String, password: String)

    suspend fun signUp(name: String, email: String, password: String)

    suspend fun sendPasswordReset(email: String)

    fun signOut()
}