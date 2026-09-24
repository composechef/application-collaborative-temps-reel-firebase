package com.composechef.testmate.feature.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R

@Composable
fun AuthScreen(
    mode: String,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: (String, String, Boolean) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    var resetSent by remember { mutableStateOf(false) }
    val validEmail = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val signup = mode == "signup"
    val reset = mode == "reset"
    Column(
        Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    stringResource(R.string.back),
                )
            }
            Icon(
                Icons.Outlined.CheckCircle,
                null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp),
            )
            Text(
                "taskmate",
                Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        if (mode == "welcome") {
            WelcomeContent(
                onNavigate = onNavigate,
                onDemo = { onContinue("Alex", "alex@example.com", true) },
            )
        } else {
            Spacer(Modifier.height(12.dp))
            Text(
                if (reset) stringResource(R.string.reset_password)
                else if (signup) stringResource(R.string.create_account)
                else stringResource(R.string.sign_in),
                style = MaterialTheme.typography.headlineMedium,
            )
            if (signup)
                OutlinedTextField(
                    name,
                    { name = it },
                    Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.your_name)) },
                    singleLine = true,
                    isError = submitted && name.isBlank(),
                    supportingText = {
                        if (submitted && name.isBlank())
                            Text(stringResource(R.string.name_required))
                    },
                )
            OutlinedTextField(
                email,
                { email = it },
                Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = submitted && !validEmail,
                supportingText = {
                    if (submitted && !validEmail) Text(stringResource(R.string.email_invalid))
                },
            )
            if (!reset) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.password)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                visible = !visible
                            }
                        ) {
                            Icon(
                                if (visible) Icons.Outlined.VisibilityOff
                                else Icons.Outlined.Visibility,
                                if (visible) stringResource(R.string.password_hide)
                                else stringResource(R.string.password_show),
                            )
                        }
                    },
                    isError = submitted && password.length < 8,
                    supportingText = {
                        if (signup || (submitted && password.length < 8))
                            Text(stringResource(R.string.password_hint))
                    },
                )
                if (!signup)
                    TextButton(
                        onClick = { onNavigate("reset") },
                        Modifier.align(Alignment.End),
                    ) {
                        Text(stringResource(R.string.forgot_password))
                    }
            }
            Button(
                onClick = {
                    submitted = true
                    if (
                        validEmail &&
                        (reset || password.length >= 8) &&
                        (!signup || name.isNotBlank())
                    ) {
                        if (reset) resetSent = true
                        else
                            onContinue(
                                name.ifBlank { email.substringBefore('@') },
                                email,
                                false,
                            )
                    }
                },
                Modifier
                    .fillMaxWidth()
                    .height(54.dp),
            ) {
                Text(
                    if (reset) stringResource(R.string.preview_reset)
                    else if (signup) stringResource(R.string.create_account)
                    else stringResource(R.string.sign_in)
                )
            }
            if (!reset)
                TextButton(
                    onClick = { onNavigate(if (signup) "signin" else "signup") },
                    Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Text(
                        if (signup) stringResource(R.string.existing_account)
                        else stringResource(R.string.create_account)
                    )
                }
            Text(
                stringResource(R.string.auth_preview),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    if (resetSent)
        AlertDialog(
            onDismissRequest = { resetSent = false },
            title = { Text(stringResource(R.string.reset_preview)) },
            text = {
                Text(stringResource(R.string.reset_preview_body, email.trim()))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        resetSent = false
                        onBack()
                    }
                ) {
                    Text(stringResource(R.string.back_sign_in))
                }
            },
        )
}
