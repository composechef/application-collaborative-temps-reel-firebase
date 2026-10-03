package com.composechef.taskmate.feature.auth

import androidx.annotation.StringRes

sealed interface AuthUiState {
    data object Idle : AuthUiState

    data object Loading : AuthUiState

    data class Error(@StringRes val message: Int) : AuthUiState
}
