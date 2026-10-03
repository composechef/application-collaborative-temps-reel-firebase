package com.composechef.taskmate.feature.tasks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.R
import com.composechef.taskmate.core.designsystem.components.TaskMateSheet
import com.composechef.taskmate.core.localization.label
import com.composechef.taskmate.core.model.DueDate
import com.composechef.taskmate.core.model.SharedList
import com.composechef.taskmate.core.model.Task
import com.composechef.taskmate.feature.tasks.components.TaskOptionRow

/** Crée ou modifie une tâche. [onSave] reçoit une copie de [task] avec les champs modifiés. */
@Composable
fun TaskEditorSheet(
    task: Task,
    list: SharedList,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit,
    onDelete: () -> Unit,
) {
    var title by rememberSaveable(task.id) { mutableStateOf(task.title) }
    var notes by rememberSaveable(task.id) { mutableStateOf(task.notes) }
    var assigneeId by rememberSaveable(task.id) { mutableStateOf(task.assigneeId) }
    var due by rememberSaveable(task.id) { mutableStateOf(task.due) }
    var assigneeMenu by remember { mutableStateOf(false) }
    var dueMenu by remember { mutableStateOf(false) }

    TaskMateSheet(onDismiss = onDismiss) {
        Text(
            text = stringResource(if (isNew) R.string.new_task else R.string.task_details),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = list.name,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it.take(160) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.task_title)) },
            maxLines = 3,
        )
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it.take(2000) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.notes_optional)) },
            minLines = 2,
            maxLines = 6,
        )
        Box {
            TaskOptionRow(
                label = stringResource(R.string.assignee),
                value =
                    list.members.find { it.id == assigneeId }?.name
                        ?: stringResource(R.string.anyone),
                onClick = { assigneeMenu = true },
            )
            DropdownMenu(expanded = assigneeMenu, onDismissRequest = { assigneeMenu = false }) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.anyone)) },
                    onClick = {
                        assigneeId = null
                        assigneeMenu = false
                    },
                )
                list.members.forEach { member ->
                    DropdownMenuItem(
                        text = { Text(text = member.name) },
                        onClick = {
                            assigneeId = member.id
                            assigneeMenu = false
                        },
                    )
                }
            }
        }
        Box {
            TaskOptionRow(
                label = stringResource(R.string.due_date),
                value = due.label(),
                onClick = { dueMenu = true },
            )
            DropdownMenu(expanded = dueMenu, onDismissRequest = { dueMenu = false }) {
                DueDate.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option.label()) },
                        onClick = {
                            due = option
                            dueMenu = false
                        },
                    )
                }
            }
        }
        Button(
            onClick = {
                onSave(
                    task.copy(
                        title = title.trim(),
                        notes = notes.trim(),
                        assigneeId = assigneeId,
                        due = due,
                    )
                )
            },
            enabled = title.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            Text(text = stringResource(if (isNew) R.string.add_task else R.string.save_changes))
        }
        if (!isNew) {
            TextButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                colors =
                    ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                Text(text = stringResource(R.string.delete_task))
            }
        }
    }
}
