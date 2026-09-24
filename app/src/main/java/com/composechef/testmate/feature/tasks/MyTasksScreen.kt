package com.composechef.testmate.feature.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R
import com.composechef.testmate.core.designsystem.components.EmptyState
import com.composechef.testmate.core.localization.label
import com.composechef.testmate.core.model.DueDate
import com.composechef.testmate.core.model.SharedCollection
import com.composechef.testmate.core.model.Task
import com.composechef.testmate.feature.tasks.components.TaskRow

@Composable
fun MyTasksScreen(
    allTasks: List<Task>,
    collections: List<SharedCollection>,
    onToggle: (String) -> Unit,
    onTask: (Task) -> Unit,
    onDeleteTask: (String) -> Unit,
) {
    var completed by rememberSaveable { mutableStateOf(false) }
    val tasks = allTasks.filter { it.assignee == "me" && it.completed == completed }
    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(stringResource(R.string.my_tasks), style = MaterialTheme.typography.headlineLarge)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    !completed,
                    { completed = false },
                    { Text(stringResource(R.string.to_do)) },
                )
                FilterChip(
                    completed,
                    { completed = true },
                    { Text(stringResource(R.string.completed)) },
                )
            }
        }
        listOf(DueDate.Today, DueDate.Tomorrow, DueDate.ThisWeek, DueDate.None).forEach { due ->
            val group = tasks.filter { it.due == due }
            if (group.isNotEmpty()) {
                item {
                    Text(
                        due.label(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
                items(group, key = { it.id }) { task ->
                    collections
                        .find { it.id == task.collectionId }
                        ?.let {
                            TaskRow(
                                task,
                                it,
                                { onToggle(task.id) },
                                { onTask(task) },
                                { onDeleteTask(task.id) },
                                true,
                            )
                        }
                }
            }
        }
        if (tasks.isEmpty())
            item {
                EmptyState(
                    if (completed) stringResource(R.string.empty_completed)
                    else stringResource(R.string.empty_mine),
                    if (completed) stringResource(R.string.empty_my_completed_description)
                    else stringResource(R.string.empty_my_tasks_description),
                )
            }
    }
}
