package com.composechef.taskmate.feature.tasks

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.R
import com.composechef.taskmate.core.designsystem.components.EmptyState
import com.composechef.taskmate.core.designsystem.components.LoadingState
import com.composechef.taskmate.core.designsystem.components.showUndoSnackbar
import com.composechef.taskmate.core.designsystem.theme.TaskMateTheme
import com.composechef.taskmate.core.localization.label
import com.composechef.taskmate.core.model.DueDate
import com.composechef.taskmate.core.model.Task
import com.composechef.taskmate.core.preview.PreviewData
import com.composechef.taskmate.feature.tasks.components.TaskRow
import kotlinx.coroutines.launch

/** Les tâches attribuées à l'utilisateur, toutes listes confondues, regroupées par échéance. */
@Composable
fun MyTasksScreen(
    uiState: MyTasksUiState,
    onOpenList: (listId: String) -> Unit,
    onSetTaskCompleted: (task: Task, completed: Boolean) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onRestoreTask: (Task) -> Unit,
    modifier: Modifier = Modifier,
) {
    val resources = LocalResources.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val toggleTask: (Task) -> Unit = { task ->
        val completed = !task.completed
        onSetTaskCompleted(task, completed)
        scope.launch {
            snackbarHostState.showUndoSnackbar(
                message =
                    resources.getString(
                        if (completed) R.string.task_completed else R.string.task_reopened
                    ),
                undoLabel = resources.getString(R.string.undo),
                onUndo = { onSetTaskCompleted(task, task.completed) },
            )
        }
    }
    val deleteTask: (Task) -> Unit = { task ->
        onDeleteTask(task)
        scope.launch {
            snackbarHostState.showUndoSnackbar(
                message = resources.getString(R.string.task_deleted),
                undoLabel = resources.getString(R.string.undo),
                duration = SnackbarDuration.Long,
                onUndo = { onRestoreTask(task) },
            )
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        when (uiState) {
            MyTasksUiState.Loading -> LoadingState(modifier = Modifier.padding(padding))
            is MyTasksUiState.Success -> MyTasksContent(
                tasks = uiState.tasks,
                padding = padding,
                onOpenList = onOpenList,
                onToggleTask = toggleTask,
                onDeleteTask = deleteTask,
            )
        }
    }
}

@Composable
private fun MyTasksContent(
    tasks: List<AssignedTask>,
    padding: PaddingValues,
    onOpenList: (listId: String) -> Unit,
    onToggleTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
) {
    var showCompleted by rememberSaveable { mutableStateOf(false) }
    val visible = tasks.filter { it.task.completed == showCompleted }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 760.dp)
                .fillMaxSize(),
            contentPadding =
                PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = padding.calculateTopPadding() + 24.dp,
                    bottom = padding.calculateBottomPadding() + 24.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item("header") {
                Text(
                    text = stringResource(R.string.my_tasks),
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
            item("filters") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = !showCompleted,
                        onClick = { showCompleted = false },
                        label = { Text(text = stringResource(R.string.to_do)) },
                    )
                    FilterChip(
                        selected = showCompleted,
                        onClick = { showCompleted = true },
                        label = { Text(text = stringResource(R.string.completed)) },
                    )
                }
            }
            listOf(DueDate.Today, DueDate.Tomorrow, DueDate.ThisWeek, DueDate.None).forEach { due ->
                val group = visible.filter { it.task.due == due }
                if (group.isNotEmpty()) {
                    item(key = due) {
                        Text(
                            text = due.label(),
                            modifier = Modifier.padding(top = 16.dp),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    items(group, key = { it.task.id }) { (task, list) ->
                        TaskRow(
                            task = task,
                            list = list,
                            onToggle = { onToggleTask(task) },
                            onOpen = { onOpenList(list.id) },
                            onDelete = { onDeleteTask(task) },
                            showListName = true,
                        )
                    }
                }
            }
            if (visible.isEmpty()) {
                item("empty-state") {
                    EmptyState(
                        title = stringResource(
                            if (showCompleted) R.string.empty_completed else R.string.empty_mine
                        ),
                        description = stringResource(
                            if (showCompleted) R.string.empty_my_completed_description
                            else R.string.empty_my_tasks_description
                        ),
                    )
                }
            }
        }
    }
}

@Preview(name = "Téléphone", widthDp = 393, heightDp = 852, showBackground = true)
@Preview(
    name = "Sombre",
    widthDp = 393,
    heightDp = 852,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun MyTasksScreenPreview() {
    val tasks =
        PreviewData.tasks
            .filter { it.assigneeId == PreviewData.user.id }
            .map { task -> AssignedTask(task, PreviewData.lists.first { it.id == task.listId }) }
    TaskMateTheme {
        MyTasksScreen(
            uiState = MyTasksUiState.Success(tasks),
            onOpenList = {},
            onSetTaskCompleted = { _, _ -> },
            onDeleteTask = {},
            onRestoreTask = {},
        )
    }
}
