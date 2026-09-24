package com.composechef.testmate.feature.collections

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R
import com.composechef.testmate.core.designsystem.components.Avatar
import com.composechef.testmate.core.designsystem.components.EmptyState
import com.composechef.testmate.core.model.SharedCollection
import com.composechef.testmate.core.model.Task
import com.composechef.testmate.feature.tasks.components.TaskRow

private enum class TaskFilter(val label: Int) {
    ToDo(R.string.to_do),
    Mine(R.string.mine),
    Completed(R.string.completed),
}

@Composable
fun CollectionScreen(
    collection: SharedCollection,
    tasks: List<Task>,
    onToggle: (String) -> Unit,
    onTask: (Task) -> Unit,
    onDeleteTask: (String) -> Unit,
    onMembers: () -> Unit,
) {
    var filter by rememberSaveable(collection.id) { mutableStateOf(TaskFilter.ToDo) }
    val filtered = tasks.filter {
        when (filter) {
            TaskFilter.ToDo -> !it.completed
            TaskFilter.Mine -> it.assignee == "me" && !it.completed
            TaskFilter.Completed -> it.completed
        }
    }
    LazyColumn(
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            TextButton(onClick = onMembers, contentPadding = PaddingValues(vertical = 8.dp)) {
                collection.members.take(3).forEachIndexed { index, member ->
                    Avatar(member, Modifier.padding(end = 4.dp), index)
                }
                val memberCount = collection.members.size
                Text(
                    pluralStringResource(R.plurals.member_count, memberCount, memberCount),
                    Modifier.padding(start = 8.dp),
                )
            }
        }
        if (collection.description.isNotBlank()) {
            item {
                Text(
                    collection.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        item {
            Text(
                pluralStringResource(
                    R.plurals.progress,
                    tasks.count { it.completed },
                    tasks.count { it.completed },
                    tasks.size,
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TaskFilter.entries.forEach { option ->
                    FilterChip(
                        selected = filter == option,
                        onClick = { filter = option },
                        label = { Text(stringResource(option.label)) },
                    )
                }
            }
        }
        items(filtered, key = { it.id }) { task ->
            TaskRow(
                task,
                collection,
                { onToggle(task.id) },
                { onTask(task) },
                { onDeleteTask(task.id) },
            )
        }
        if (filtered.isEmpty()) {
            item {
                EmptyState(
                    title =
                        when (filter) {
                            TaskFilter.ToDo -> stringResource(R.string.empty_to_do)
                            TaskFilter.Mine -> stringResource(R.string.empty_mine)
                            TaskFilter.Completed -> stringResource(R.string.empty_completed)
                        },
                    description =
                        when (filter) {
                            TaskFilter.ToDo -> stringResource(R.string.empty_to_do_description)
                            TaskFilter.Mine -> stringResource(R.string.empty_mine_description)
                            TaskFilter.Completed ->
                                stringResource(R.string.empty_completed_description)
                        },
                )
            }
        }
    }
}
