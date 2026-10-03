package com.composechef.taskmate.feature.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.composechef.taskmate.feature.auth.components.PasswordField
import com.composechef.taskmate.feature.auth.components.isValidEmail
import com.composechef.taskmate.feature.auth.components.isValidPassword

@Composable
fun SignInScreen(
    uiState: AuthUiState,
    onSignIn: (email: String, password: String) -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }
    val emailValid = isValidEmail(email)
    val passwordValid = isValidPassword(password)

    AuthLayout(onBack = onBack, modifier = modifier) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.sign_in),
            style = MaterialTheme.typography.headlineMedium
        )

        EmailField(
            value = email,
            onValueChange = { email = it },
            showError = submitted && !emailValid
        )

        PasswordField(
            value = password,
            onValueChange = { password = it },
            showError = submitted && !passwordValid,
        )

        TextButton(onClick = onForgotPassword, modifier = Modifier.align(Alignment.End)) {
            Text(text = stringResource(R.string.forgot_password))
        }

        AuthErrorMessage(message = (uiState as? AuthUiState.Error)?.message)

        LoadingButton(
            text = stringResource(R.string.sign_in),
            isLoading = uiState is AuthUiState.Loading,
            onClick = {
                submitted = true
                if (emailValid && passwordValid) onSignIn(email.trim(), password)
            },
        )
        
        TextButton(
            onClick = onCreateAccount,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.create_account))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    TaskMateTheme {
        SignInScreen(
            uiState = AuthUiState.Idle,
            onSignIn = { _, _ -> },
            onForgotPassword = {},
            onCreateAccount = {},
            onBack = {},
        )
    }
}

@Preview(name = "Erreur", showBackground = true)
@Composable
private fun SignInScreenErrorPreview() {
    TaskMateTheme {
        SignInScreen(
            uiState = AuthUiState.Error(R.string.error_invalid_credentials),
            onSignIn = { _, _ -> },
            onForgotPassword = {},
            onCreateAccount = {},
            onBack = {},
        )
    }
}
