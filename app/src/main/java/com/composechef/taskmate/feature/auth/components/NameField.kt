package com.composechef.taskmate.feature.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.composechef.taskmate.R

@Composable
internal fun NameField(value: String, onValueChange: (String) -> Unit, showError: Boolean) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(R.string.your_name)) },
        singleLine = true,
        isError = showError,
        supportingText = {
            if (showError) Text(text = stringResource(R.string.name_required))
        },
    )
}
