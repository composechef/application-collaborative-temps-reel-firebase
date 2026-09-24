package com.composechef.taskmate.feature.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.R
import com.composechef.taskmate.core.designsystem.components.Avatar
import com.composechef.taskmate.core.designsystem.components.TaskMateSheet
import com.composechef.taskmate.core.model.Member

@Composable
fun MembersSheet(members: List<Member>, currentUserId: String, onDismiss: () -> Unit) {
    TaskMateSheet(onDismiss = onDismiss) {
        Text(text = stringResource(R.string.members), style = MaterialTheme.typography.titleLarge)
        members.forEachIndexed { index, member ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(member = member, index = index)
                Text(
                    text = member.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (member.id == currentUserId) {
                    Text(
                        text = stringResource(R.string.you),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
