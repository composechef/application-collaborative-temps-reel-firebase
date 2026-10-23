package com.composechef.taskmate.navigation

import com.composechef.taskmate.core.model.User

sealed interface SessionState {
    data object Loading : SessionState

    data object SignedOut : SessionState

    data class SignedIn(val user: User) : SessionState
}
