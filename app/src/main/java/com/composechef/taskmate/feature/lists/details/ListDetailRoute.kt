package com.composechef.taskmate.feature.lists.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect


@Composable
fun ListDetailRoute(listId: String, onBack: () -> Unit) {
    val viewModel = hiltViewModel<ListDetailViewModel, ListDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(listId) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is ListDetailUiState.NotFound) onBack()
    }

    ListDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onSaveTask = viewModel::saveTask,
        onSetTaskCompleted = viewModel::setTaskCompleted,
        onDeleteTask = viewModel::deleteTask,
        onDeleteList = viewModel::deleteList,
    )
}
