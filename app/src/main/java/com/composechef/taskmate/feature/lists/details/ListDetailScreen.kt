package com.composechef.taskmate.feature.lists.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.R
import com.composechef.taskmate.core.designsystem.components.EmptyState
import com.composechef.taskmate.core.designsystem.components.LoadingState
import com.composechef.taskmate.core.designsystem.components.showUndoSnackbar
import com.composechef.taskmate.core.designsystem.theme.TaskMateTheme
import com.composechef.taskmate.core.model.SharedList
import com.composechef.taskmate.core.model.Task
import com.composechef.taskmate.core.preview.PreviewData
import com.composechef.taskmate.feature.lists.modals.MembersSheet
import com.composechef.taskmate.feature.lists.TaskFilter
import com.composechef.taskmate.feature.lists.components.MemberAvatars
import com.composechef.taskmate.feature.lists.components.TaskFilterChips
import com.composechef.taskmate.feature.lists.modals.DeleteListDialog
import com.composechef.taskmate.feature.lists.modals.InviteSheet
import com.composechef.taskmate.feature.lists.modals.ListSheet
import com.composechef.taskmate.feature.tasks.TaskEditorSheet
import com.composechef.taskmate.feature.tasks.components.TaskRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreen(
    uiState: ListDetailUiState,
    onBack: () -> Unit,
    onSaveTask: (Task) -> Unit,
    onSetTaskCompleted: (task: Task, completed: Boolean) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onDeleteList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val resources = LocalResources.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var sheet by remember { mutableStateOf<ListSheet?>(null) }
    var menuExpanded by remember { mutableStateOf(false) }
    var confirmDelete by rememberSaveable { mutableStateOf(false) }
    val success = uiState as? ListDetailUiState.Success

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
                onUndo = { onSaveTask(task) },
            )
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = success?.list?.name.orEmpty(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back_lists),
                        )
                    }
                },
                actions = {
                    if (success != null) {
                        IconButton(onClick = { sheet = ListSheet.Invite }) {
                            Icon(
                                imageVector = Icons.Outlined.PersonAdd,
                                contentDescription = stringResource(R.string.invite_members),
                            )
                        }
                        // Seul le propriétaire peut supprimer la liste.
                        if (success.list.ownerId == success.currentUserId) {
                            Box {
                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(
                                        imageVector = Icons.Outlined.MoreVert,
                                        contentDescription = stringResource(R.string.list_options),
                                    )
                                }
                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false },
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = stringResource(R.string.delete_list)) },
                                        onClick = {
                                            menuExpanded = false
                                            confirmDelete = true
                                        },
                                    )
                                }
                            }
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (success != null) {
                ExtendedFloatingActionButton(
                    onClick = {
                        sheet = ListSheet.EditTask(Task(listId = success.list.id), isNew = true)
                    },
                    icon = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) },
                    text = { Text(text = stringResource(R.string.add_task)) },
                )
            }
        },
    ) { padding ->
        when (uiState) {
            ListDetailUiState.Loading -> LoadingState(
                modifier = Modifier.padding(padding)
            )

            ListDetailUiState.NotFound -> EmptyState(
                title = stringResource(R.string.list_not_found),
                description = stringResource(R.string.list_not_found_description),
                modifier = Modifier.padding(padding),
            )

            is ListDetailUiState.Success -> ListDetailContent(
                list = uiState.list,
                tasks = uiState.tasks,
                currentUserId = uiState.currentUserId,
                padding = padding,
                onOpenTask = { sheet = ListSheet.EditTask(it, isNew = false) },
                onToggleTask = toggleTask,
                onDeleteTask = deleteTask,
                onShowMembers = { sheet = ListSheet.Members },
            )
        }
    }

    if (success != null) {
        when (val activeSheet = sheet) {
            ListSheet.Invite -> InviteSheet(
                list = success.list,
                onDismiss = { sheet = null }
            )

            ListSheet.Members -> MembersSheet(
                members = success.list.members,
                currentUserId = success.currentUserId,
                onDismiss = { sheet = null },
            )

            is ListSheet.EditTask -> TaskEditorSheet(
                task = activeSheet.task,
                list = success.list,
                isNew = activeSheet.isNew,
                onDismiss = { sheet = null },
                onSave = { task ->
                    sheet = null
                    onSaveTask(task)
                },
                onDelete = {
                    sheet = null
                    deleteTask(activeSheet.task)
                },
            )

            null -> Unit
        }
        if (confirmDelete) {
            DeleteListDialog(
                name = success.list.name,
                onDismiss = { confirmDelete = false },
                onConfirm = {
                    confirmDelete = false
                    onDeleteList()
                },
            )
        }
    }
}

@Composable
private fun ListDetailContent(
    list: SharedList,
    tasks: List<Task>,
    currentUserId: String,
    padding: PaddingValues,
    onOpenTask: (Task) -> Unit,
    onToggleTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onShowMembers: () -> Unit,
) {
    var filter by rememberSaveable(list.id) { mutableStateOf(TaskFilter.ToDo) }
    val filtered = tasks.filter { filter.matches(it, currentUserId) }
    val completedCount = tasks.count { it.completed }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 760.dp)
                .fillMaxSize(),
            contentPadding =
                PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 96.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item("members") {
                TextButton(
                    onClick = onShowMembers,
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    MemberAvatars(members = list.members)
                    Text(
                        text = pluralStringResource(
                            R.plurals.member_count,
                            list.members.size,
                            list.members.size,
                        ),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
            if (list.description.isNotBlank()) {
                item("description") {
                    Text(
                        text = list.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            item("completed-task") {
                Text(
                    text = pluralStringResource(
                        R.plurals.progress,
                        completedCount,
                        completedCount,
                        tasks.size,
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            item("task-filter") {
                TaskFilterChips(selected = filter, onSelect = { filter = it })
            }
            items(filtered, key = { it.id }) { task ->
                TaskRow(
                    task = task,
                    list = list,
                    onToggle = { onToggleTask(task) },
                    onOpen = { onOpenTask(task) },
                    onDelete = { onDeleteTask(task) },
                )
            }
            if (filtered.isEmpty()) {
                item("empty-state") {
                    EmptyState(
                        title = stringResource(filter.emptyTitle),
                        description = stringResource(filter.emptyDescription),
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
private fun ListDetailScreenPreview() {
    TaskMateTheme {
        ListDetailScreen(
            uiState =
                ListDetailUiState.Success(
                    list = PreviewData.launch,
                    tasks = PreviewData.tasks.filter { it.listId == PreviewData.launch.id },
                    currentUserId = PreviewData.user.id,
                ),
            onBack = {},
            onSaveTask = {},
            onSetTaskCompleted = { _, _ -> },
            onDeleteTask = {},
            onDeleteList = {},
        )
    }
}
