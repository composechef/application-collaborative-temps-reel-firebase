package com.composechef.testmate.feature.collections

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composechef.testmate.R
import com.composechef.testmate.core.designsystem.components.WorkspaceSheet
import com.composechef.testmate.core.model.SharedCollection

@Composable
fun InviteSheet(collection: SharedCollection, onDismiss: () -> Unit) {
    val resources = LocalResources.current
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }
    WorkspaceSheet(onDismiss) {
        Text(resources.getString(R.string.invite), style = MaterialTheme.typography.titleLarge)
        Text(collection.name, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(
                collection.code,
                style = MaterialTheme.typography.headlineLarge,
                letterSpacing = 6.sp,
                modifier = Modifier.fillMaxWidth().padding(24.dp),
            )
        }
        Button(
            onClick = {
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                    .setPrimaryClip(
                        ClipData.newPlainText(
                            resources.getString(R.string.invitation_label),
                            collection.code,
                        )
                    )
                copied = true
            },
            Modifier.fillMaxWidth(),
        ) {
            Text(
                if (copied) resources.getString(R.string.copied)
                else resources.getString(R.string.copy)
            )
        }
        OutlinedButton(
            onClick = {
                context.startActivity(
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                resources.getString(
                                    R.string.share_invitation_body,
                                    collection.name,
                                    collection.code,
                                ),
                            )
                        },
                        resources.getString(R.string.share_invitation),
                    )
                )
            },
            Modifier.fillMaxWidth(),
        ) {
            Text(resources.getString(R.string.share))
        }
        Text(
            resources.getString(R.string.invitation_preview),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
