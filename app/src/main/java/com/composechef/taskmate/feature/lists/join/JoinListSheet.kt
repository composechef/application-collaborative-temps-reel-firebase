package com.composechef.taskmate.feature.lists.join

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.composechef.taskmate.R
import com.composechef.taskmate.core.designsystem.components.LoadingButton
import com.composechef.taskmate.core.designsystem.components.TaskMateSheet
import com.composechef.taskmate.core.model.ListInvite
import com.composechef.taskmate.feature.lists.components.InvitePreview


@Composable
fun JoinListSheet(
    uiState: JoinListUiState,
    onSearch: (code: String) -> Unit,
    onJoin: (ListInvite) -> Unit,
    onDismiss: () -> Unit,
) {
    var code by rememberSaveable { mutableStateOf("") }
    var searchedCode by rememberSaveable { mutableStateOf<String?>(null) }
    // Un résultat ne vaut que pour le code recherché : dès qu'on modifie le code, on l'ignore.
    val result = if (searchedCode == code) uiState else JoinListUiState.Idle

    TaskMateSheet(onDismiss = onDismiss) {
        Text(
            text = stringResource(R.string.join_list),
            style = MaterialTheme.typography.headlineMedium,
        )
        OutlinedTextField(
            value = code,
            onValueChange = { input ->
                code = input.uppercase().filter(Char::isLetterOrDigit).take(ListInvite.CODE_LENGTH)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.invitation_code)) },
            singleLine = true,
            isError = result is JoinListUiState.NotFound,
            supportingText = {
                Text(
                    text = if (result is JoinListUiState.NotFound) {
                        stringResource(R.string.invalid_code)
                    } else {
                        stringResource(R.string.code_hint)
                    }
                )
            },
        )
        when (result) {
            is JoinListUiState.Found -> {
                InvitePreview(invite = result.invite)
                LoadingButton(
                    text = stringResource(R.string.join_list),
                    isLoading = result.isJoining,
                    onClick = { onJoin(result.invite) },
                )
            }

            else -> LoadingButton(
                text = stringResource(R.string.find_list),
                isLoading = result is JoinListUiState.Searching,
                enabled = code.length == ListInvite.CODE_LENGTH,
                onClick = {
                    searchedCode = code
                    onSearch(code)
                },
            )
        }
    }
}
