package com.composechef.taskmate.core.designsystem.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

/** Affiche un snackbar avec une action « Annuler » et appelle [onUndo] si on la touche. */
suspend fun SnackbarHostState.showUndoSnackbar(
    message: String,
    undoLabel: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onUndo: () -> Unit,
) {
    currentSnackbarData?.dismiss()
    val result = showSnackbar(message = message, actionLabel = undoLabel, duration = duration)
    if (result == SnackbarResult.ActionPerformed) onUndo()
}
