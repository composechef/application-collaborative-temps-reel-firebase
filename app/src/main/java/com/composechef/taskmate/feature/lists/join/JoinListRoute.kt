package com.composechef.taskmate.feature.lists.join

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun JoinListRoute(
    onDismiss: () -> Unit,
    onJoined: (listId: String) -> Unit,
    viewModel: JoinListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        val joined = uiState as? JoinListUiState.Joined ?: return@LaunchedEffect
        viewModel.reset()
        onJoined(joined.listId)
    }

    JoinListSheet(
        uiState = uiState,
        onSearch = viewModel::search,
        onJoin = viewModel::join,
        onDismiss = {
            viewModel.reset()
            onDismiss()
        },
    )
}
