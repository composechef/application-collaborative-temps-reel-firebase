package com.composechef.taskmate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TaskMateNavigation(sessionViewModel: SessionViewModel = hiltViewModel()) {
    val session by sessionViewModel.session.collectAsStateWithLifecycle()

    when (val state = session) {
        SessionState.Loading -> Unit
        SessionState.SignedOut -> AuthNavigation()
        is SessionState.SignedIn -> MainNavigation(user = state.user)
    }
}
