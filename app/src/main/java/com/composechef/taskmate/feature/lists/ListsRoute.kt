package com.composechef.taskmate.feature.lists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.composechef.taskmate.feature.lists.join.JoinListRoute

@Composable
fun ListsRoute(onOpenList: (listId: String) -> Unit, viewModel: ListsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showJoinSheet by rememberSaveable { mutableStateOf(false) }

    ListsScreen(
        uiState = uiState,
        onOpenList = onOpenList,
        onCreateList = viewModel::createList,
        onJoinList = { showJoinSheet = true },
    )

    if (showJoinSheet) {
        JoinListRoute(
            onDismiss = { showJoinSheet = false },
            onJoined = { listId ->
                showJoinSheet = false
                onOpenList(listId)
            },
        )
    }
}
