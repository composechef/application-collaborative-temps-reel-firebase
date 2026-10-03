package com.composechef.taskmate.feature.lists.modals

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composechef.taskmate.R
import com.composechef.taskmate.core.designsystem.components.TaskMateSheet
import com.composechef.taskmate.core.model.SharedList

@Composable
fun InviteSheet(list: SharedList, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val resources = LocalResources.current
    var copied by remember { mutableStateOf(false) }

    TaskMateSheet(onDismiss = onDismiss) {
        Text(text = stringResource(R.string.invite), style = MaterialTheme.typography.titleLarge)

        Text(
            text = stringResource(
                R.string.invite_description,
                list.name
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(
                text = list.code,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                style = MaterialTheme.typography.headlineLarge,
                letterSpacing = 6.sp,
            )
        }
        Button(
            onClick = {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(
                    ClipData.newPlainText(resources.getString(R.string.invitation_label), list.code)
                )
                copied = true
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(if (copied) R.string.copied else R.string.copy))
        }
        OutlinedButton(
            onClick = {
                val send = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        resources.getString(
                            R.string.share_invitation_body,
                            list.name,
                            list.code
                        ),
                    )
                }
                context.startActivity(
                    Intent.createChooser(send, resources.getString(R.string.share_invitation))
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.share))
        }
    }
}
