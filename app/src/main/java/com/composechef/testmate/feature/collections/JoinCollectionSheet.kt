package com.composechef.testmate.feature.collections

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R
import com.composechef.testmate.core.designsystem.components.WorkspaceSheet

@Composable
fun JoinCollectionSheet(onDismiss: () -> Unit, onJoin: () -> Unit) {
    var code by rememberSaveable { mutableStateOf("") }
    var searched by rememberSaveable { mutableStateOf(false) }
    val valid = code.trim().uppercase() == "TOGETH"
    WorkspaceSheet(onDismiss) {
        Text(
            stringResource(R.string.join_collection),
            style = MaterialTheme.typography.headlineMedium,
        )
        OutlinedTextField(
            code,
            {
                code = it.uppercase().filter(Char::isLetterOrDigit).take(6)
                searched = false
            },
            Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(stringResource(R.string.invitation_code)) },
            isError = searched && !valid,
            supportingText = {
                Text(
                    if (searched && !valid) stringResource(R.string.invalid_code)
                    else stringResource(R.string.demo_code)
                )
            },
        )
        if (searched && valid) {
            Text(stringResource(R.string.garden), style = MaterialTheme.typography.titleMedium)
            Button(onClick = onJoin, Modifier.fillMaxWidth().height(52.dp)) {
                Text(stringResource(R.string.join_collection))
            }
        } else
            Button(
                onClick = { searched = true },
                enabled = code.length == 6,
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text(stringResource(R.string.find_collection))
            }
    }
}
