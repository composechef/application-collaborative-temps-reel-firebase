package com.composechef.taskmate.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun SignUpRoute(
    onSignIn: () -> Unit,
    onBack: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SignUpScreen(
        uiState = uiState,
        onSignUp = viewModel::signUp,
        onSignIn = onSignIn,
        onBack = onBack,
    )
}
