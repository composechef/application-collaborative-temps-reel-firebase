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
import com.composechef.taskmate.feature.auth.components.NameField
import com.composechef.taskmate.feature.auth.components.PasswordField
import com.composechef.taskmate.feature.auth.components.isValidEmail
import com.composechef.taskmate.feature.auth.components.isValidPassword

@Composable
fun SignUpScreen(
    uiState: AuthUiState,
    onSignUp: (name: String, email: String, password: String) -> Unit,
    onSignIn: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }
    val nameValid = name.isNotBlank()
    val emailValid = isValidEmail(email)
    val passwordValid = isValidPassword(password)

    AuthLayout(onBack = onBack, modifier = modifier) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.create_account),
            style = MaterialTheme.typography.headlineMedium,
        )
        NameField(value = name, onValueChange = { name = it }, showError = submitted && !nameValid)
        EmailField(value = email, onValueChange = { email = it }, showError = submitted && !emailValid)
        PasswordField(
            value = password,
            onValueChange = { password = it },
            showError = submitted && !passwordValid,
            showHint = true,
        )
        AuthErrorMessage(message = (uiState as? AuthUiState.Error)?.message)
        LoadingButton(
            text = stringResource(R.string.create_account),
            isLoading = uiState is AuthUiState.Loading,
            onClick = {
                submitted = true
                if (nameValid && emailValid && passwordValid) {
                    onSignUp(name.trim(), email.trim(), password)
                }
            },
        )
        TextButton(onClick = onSignIn, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = stringResource(R.string.existing_account))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    TaskMateTheme {
        SignUpScreen(
            uiState = AuthUiState.Idle,
            onSignUp = { _, _, _ -> },
            onSignIn = {},
            onBack = {},
        )
    }
}
