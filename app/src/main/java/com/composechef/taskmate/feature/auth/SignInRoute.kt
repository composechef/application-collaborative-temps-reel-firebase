package com.composechef.taskmate.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SignInRoute(
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    onBack: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SignInScreen(
        uiState = uiState,
        onSignIn = viewModel::signIn,
        onForgotPassword = onForgotPassword,
        onCreateAccount = onCreateAccount,
        onBack = onBack,
    )
}
