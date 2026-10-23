package com.composechef.taskmate.feature.profile

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.composechef.taskmate.core.model.User

@Composable
fun ProfileRoute(user: User, viewModel: ProfileViewModel = hiltViewModel()) {
    ProfileScreen(
        name = user.name,
        email = user.email,
        onSignOut = viewModel::signOut
    )
}