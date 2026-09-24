package com.composechef.testmate.feature.profile

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.composechef.testmate.R

@Composable
internal fun SignOutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.sign_out_title)) },
        text = {
            Text(stringResource(R.string.sign_out_description))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.sign_out)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.stay)) } },
    )
}
