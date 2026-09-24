package com.composechef.testmate.feature.tasks.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.composechef.testmate.core.designsystem.components.Avatar
import com.composechef.testmate.core.designsystem.components.SwipeActions
import com.composechef.testmate.core.localization.label
import com.composechef.testmate.core.model.DueDate
import com.composechef.testmate.core.model.SharedCollection
import com.composechef.testmate.core.model.Task

@Composable
fun TaskRow(
    task: Task,
    collection: SharedCollection,
    onToggle: () -> Unit,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    showCollection: Boolean = false,
) {
    SwipeActions(
        modifier = Modifier.testTag("task-${task.id}"),
        onDelete = onDelete,
        onComplete = if (task.completed) null else onToggle,
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Row(
                Modifier.fillMaxWidth()
                    .clickable(onClick = onOpen)
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = task.completed, onCheckedChange = { onToggle() })
                Column(
                    Modifier.weight(1f).padding(end = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (task.completed) TextDecoration.LineThrough else null,
                        color =
                            if (task.completed) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface,
                    )
                    val caption =
                        listOfNotNull(
                                if (showCollection) collection.name else null,
                                task.due.takeUnless { it == DueDate.None }?.label(),
                            )
                            .joinToString(" · ")
                    if (caption.isNotEmpty())
                        Text(
                            caption,
                            style = MaterialTheme.typography.labelSmall,
                            color =
                                if (task.due == DueDate.Today && !task.completed)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                }
                collection.members
                    .find { it.id == task.assignee }
                    ?.let {
                        Avatar(it, Modifier.padding(end = 8.dp), collection.members.indexOf(it))
                    }
            }
        }
    }
}
