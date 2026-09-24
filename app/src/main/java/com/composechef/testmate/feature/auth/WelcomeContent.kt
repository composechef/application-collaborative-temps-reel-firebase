package com.composechef.testmate.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R

@Composable
internal fun WelcomeContent(onNavigate: (String) -> Unit, onDemo: () -> Unit) {
    Column(
        modifier = Modifier.padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.welcome_description),
            modifier = Modifier.padding(bottom = 20.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Button(
            onClick = { onNavigate("signup") },
            Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(text = stringResource(R.string.get_started))
        }

        OutlinedButton(
            onClick = { onNavigate("signin") }, Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(text = stringResource(R.string.sign_in))
        }
        TextButton(
            onClick = onDemo,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.try_demo))
        }
    }
}
