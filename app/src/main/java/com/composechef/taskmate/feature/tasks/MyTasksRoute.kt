package com.composechef.taskmate.feature.tasks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MyTasksRoute(
    onOpenList: (listId: String) -> Unit,
    viewModel: MyTasksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MyTasksScreen(
        uiState = uiState,
        onOpenList = onOpenList,
        onSetTaskCompleted = viewModel::setTaskCompleted,
        onDeleteTask = viewModel::deleteTask,
        onRestoreTask = viewModel::restoreTask,
    )
}
