package com.composechef.testmate.feature.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R
import com.composechef.testmate.core.designsystem.components.WorkspaceSheet
import com.composechef.testmate.core.localization.label
import com.composechef.testmate.core.model.DueDate
import com.composechef.testmate.core.model.SharedCollection
import com.composechef.testmate.core.model.Task
import com.composechef.testmate.feature.tasks.components.TaskOptionRow

@Composable
fun TaskEditorSheet(
    task: Task,
    collection: SharedCollection,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit,
    isNew: Boolean,
    onDelete: () -> Unit,
) {
    var title by rememberSaveable(task.id) { mutableStateOf(task.title) }
    var notes by rememberSaveable(task.id) { mutableStateOf(task.notes) }
    var assignee by rememberSaveable(task.id) { mutableStateOf(task.assignee) }
    var due by rememberSaveable(task.id) { mutableStateOf(task.due) }
    var assigneeMenu by remember { mutableStateOf(false) }
    var dueMenu by remember { mutableStateOf(false) }
    WorkspaceSheet(onDismiss) {
        Text(
            if (isNew) stringResource(R.string.new_task) else stringResource(R.string.task_details),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            collection.name,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
        )
        OutlinedTextField(
            title,
            { title = it.take(160) },
            Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.task_title)) },
            maxLines = 3,
        )
        OutlinedTextField(
            notes,
            { notes = it.take(2000) },
            Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.notes_optional)) },
            minLines = 2,
            maxLines = 6,
        )
        Box {
            TaskOptionRow(
                label = stringResource(R.string.assignee),
                value =
                    collection.members.find { it.id == assignee }?.name
                        ?: stringResource(R.string.anyone),
                onClick = { assigneeMenu = true },
            )
            DropdownMenu(assigneeMenu, { assigneeMenu = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.anyone)) },
                    onClick = {
                        assignee = null
                        assigneeMenu = false
                    },
                )
                collection.members.forEach { member ->
                    DropdownMenuItem(
                        text = { Text(member.name) },
                        onClick = {
                            assignee = member.id
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
            DropdownMenu(dueMenu, { dueMenu = false }) {
                DueDate.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.label()) },
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
                        assignee = assignee,
                        due = due,
                    )
                )
            },
            enabled = title.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(52.dp),
        ) {
            Text(
                if (isNew) stringResource(R.string.add_task)
                else stringResource(R.string.save_changes)
            )
        }
        if (!isNew) {
            TextButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                colors =
                    ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                Text(stringResource(R.string.delete_task))
            }
        }
    }
}
