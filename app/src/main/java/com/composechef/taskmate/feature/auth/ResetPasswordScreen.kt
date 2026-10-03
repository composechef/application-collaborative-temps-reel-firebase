package com.composechef.taskmate.feature.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.R
import com.composechef.taskmate.core.designsystem.components.LoadingButton
import com.composechef.taskmate.core.designsystem.theme.TaskMateTheme
import com.composechef.taskmate.feature.auth.components.AuthErrorMessage
import com.composechef.taskmate.feature.auth.components.AuthLayout
import com.composechef.taskmate.feature.auth.components.EmailField
import com.composechef.taskmate.feature.auth.components.isValidEmail

@Composable
fun ResetPasswordScreen(
    uiState: ResetPasswordUiState,
    onSendResetEmail: (email: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }
    val emailValid = isValidEmail(email)

    AuthLayout(onBack = onBack, modifier = modifier) {
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        Text(
            text = stringResource(R.string.reset_password),
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            text = stringResource(R.string.reset_password_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        EmailField(
            value = email,
            onValueChange = { email = it },
            showError = submitted && !emailValid
        )

        AuthErrorMessage(
            message = (uiState as? ResetPasswordUiState.Error)?.message
        )
        LoadingButton(
            text = stringResource(R.string.send_reset_email),
            isLoading = uiState is ResetPasswordUiState.Sending,
            onClick = {
                submitted = true
                if (emailValid) onSendResetEmail(email.trim())
            },
        )
    }

    if (uiState is ResetPasswordUiState.EmailSent) {
        AlertDialog(
            onDismissRequest = onBack,
            title = {
                Text(
                    text = stringResource(R.string.reset_email_sent_title)
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.reset_email_sent_body,
                        uiState.email
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = onBack) {
                    Text(
                        text = stringResource(R.string.back_sign_in)
                    )
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ResetPasswordScreenPreview() {
    TaskMateTheme {
        ResetPasswordScreen(
            uiState = ResetPasswordUiState.Idle,
            onSendResetEmail = {},
            onBack = {}
        )
    }
}
