package com.composechef.testmate.feature.collections.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R
import com.composechef.testmate.core.designsystem.components.Avatar
import com.composechef.testmate.core.designsystem.theme.CollectionAccents
import com.composechef.testmate.core.designsystem.theme.CollectionColors
import com.composechef.testmate.core.designsystem.theme.Ink
import com.composechef.testmate.core.model.SharedCollection
import com.composechef.testmate.core.model.Task

@Composable
fun CollectionCard(collection: SharedCollection, tasks: List<Task>, onClick: () -> Unit) {
    val done = tasks.count { it.completed }
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = CollectionColors[collection.color]),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        collection.name,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        color = Ink,
                    )
                    Text(
                        pluralStringResource(
                            R.plurals.tasks_remaining,
                            tasks.size - done,
                            tasks.size - done,
                        ),
                        modifier = Modifier.padding(start = 12.dp),
                        color = CollectionAccents[collection.color],
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Text(
                    collection.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Ink.copy(alpha = .65f),
                )
            }
            LinearProgressIndicator(
                progress = { if (tasks.isEmpty()) 0f else done.toFloat() / tasks.size },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = CollectionAccents[collection.color],
                trackColor = Color.White.copy(alpha = .75f),
                drawStopIndicator = {},
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                collection.members.take(3).forEachIndexed { index, member ->
                    Avatar(member, Modifier.padding(end = 4.dp), index)
                }
                Spacer(Modifier.weight(1f))
                Text(
                    pluralStringResource(R.plurals.card_progress, done, done, tasks.size),
                    style = MaterialTheme.typography.labelMedium,
                    color = Ink.copy(alpha = .65f),
                )
            }
        }
    }
}
