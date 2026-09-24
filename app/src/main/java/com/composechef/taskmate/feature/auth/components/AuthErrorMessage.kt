package com.composechef.taskmate.feature.auth.components

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/** Le message d'erreur renvoyé par le ViewModel. N'affiche rien quand [message] est `null`. */
@Composable
internal fun AuthErrorMessage(@StringRes message: Int?) {
    if (message != null) {
        Text(
            text = stringResource(message),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
