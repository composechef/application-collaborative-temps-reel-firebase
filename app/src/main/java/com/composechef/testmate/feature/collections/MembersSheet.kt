package com.composechef.testmate.feature.collections

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R
import com.composechef.testmate.core.designsystem.components.Avatar
import com.composechef.testmate.core.designsystem.components.WorkspaceSheet
import com.composechef.testmate.core.model.SharedCollection

@Composable
fun MembersSheet(collection: SharedCollection, onDismiss: () -> Unit) {
    WorkspaceSheet(onDismiss) {
        Text(stringResource(R.string.members), style = MaterialTheme.typography.titleLarge)
        collection.members.forEachIndexed { index, member ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(member, index = index)
                Text(
                    member.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (member.id == "me")
                    Text(
                        stringResource(R.string.you),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
            }
        }
    }
}
