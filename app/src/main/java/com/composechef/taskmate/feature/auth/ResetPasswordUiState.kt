package com.composechef.taskmate.feature.auth

import androidx.annotation.StringRes

sealed interface ResetPasswordUiState {
    data object Idle : ResetPasswordUiState

    data object Sending : ResetPasswordUiState

    data class Error(@StringRes val message: Int) : ResetPasswordUiState

    data class EmailSent(val email: String) : ResetPasswordUiState
}
