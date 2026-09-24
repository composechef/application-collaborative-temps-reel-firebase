package com.composechef.taskmate.feature.auth

import androidx.annotation.StringRes

/**
 * L'état de l'envoi d'un formulaire de connexion ou d'inscription. Les valeurs des champs restent
 * dans l'écran : ici, on ne décrit que ce qui se passe après l'appui sur le bouton.
 */
sealed interface AuthUiState {
    data object Idle : AuthUiState

    data object Loading : AuthUiState

    data class Error(@StringRes val message: Int) : AuthUiState
}
