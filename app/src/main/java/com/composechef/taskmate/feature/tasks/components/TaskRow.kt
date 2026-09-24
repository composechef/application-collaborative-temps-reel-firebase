package com.composechef.taskmate.feature.tasks.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.core.designsystem.components.Avatar
import com.composechef.taskmate.core.designsystem.components.SwipeActions
import com.composechef.taskmate.core.designsystem.theme.TaskMateTheme
import com.composechef.taskmate.core.localization.label
import com.composechef.taskmate.core.model.DueDate
import com.composechef.taskmate.core.model.SharedList
import com.composechef.taskmate.core.model.Task
import com.composechef.taskmate.core.preview.PreviewData

/**
 * Une tâche dans une liste. Glisser vers la droite la termine, vers la gauche révèle « Supprimer ».
 *
 * @param showListName affiche le nom de la liste sous le titre, utile dans « Mes tâches ».
 */
@Composable
fun TaskRow(
    task: Task,
    list: SharedList,
    onToggle: () -> Unit,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    showListName: Boolean = false,
) {
    SwipeActions(
        onDelete = onDelete,
        modifier = modifier.testTag("task-${task.id}"),
        onComplete = if (task.completed) null else onToggle,
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth()
                        .clickable(onClick = onOpen)
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = task.completed, onCheckedChange = { onToggle() })
                Column(
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (task.completed) TextDecoration.LineThrough else null,
                        color =
                            if (task.completed) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface,
                    )
                    val caption =
                        listOfNotNull(
                                if (showListName) list.name else null,
                                task.due.takeUnless { it == DueDate.None }?.label(),
                            )
                            .joinToString(separator = " · ")
                    if (caption.isNotEmpty()) {
                        Text(
                            text = caption,
                            style = MaterialTheme.typography.labelSmall,
                            color =
                                if (task.due == DueDate.Today && !task.completed)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                val assigneeIndex = list.members.indexOfFirst { it.id == task.assigneeId }
                if (assigneeIndex >= 0) {
                    Avatar(
                        member = list.members[assigneeIndex],
                        modifier = Modifier.padding(end = 8.dp),
                        index = assigneeIndex,
                    )
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskRowPreview() {
    TaskMateTheme {
        TaskRow(
            task = PreviewData.tasks.first(),
            list = PreviewData.launch,
            onToggle = {},
            onOpen = {},
            onDelete = {},
            showListName = true,
        )
    }
}
