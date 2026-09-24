package com.composechef.taskmate.feature.lists

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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.R
import com.composechef.taskmate.core.designsystem.components.EmptyState
import com.composechef.taskmate.core.designsystem.components.LoadingState
import com.composechef.taskmate.core.designsystem.theme.TaskMateTheme
import com.composechef.taskmate.core.model.SharedList
import com.composechef.taskmate.core.preview.PreviewData
import com.composechef.taskmate.feature.lists.components.ListCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(
    uiState: ListsUiState,
    onOpenList: (listId: String) -> Unit,
    onCreateList: (name: String, description: String, color: Int) -> Unit,
    onJoinList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showCreateSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.lists)) },
                actions = {
                    TextButton(onClick = onJoinList) { Text(text = stringResource(R.string.join_code)) }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateSheet = true },
                icon = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) },
                text = { Text(text = stringResource(R.string.new_list)) },
            )
        },
    ) { padding ->
        when (uiState) {
            ListsUiState.Loading -> LoadingState(modifier = Modifier.padding(padding))
            is ListsUiState.Success ->
                ListsContent(lists = uiState.lists, padding = padding, onOpenList = onOpenList)
        }
    }

    if (showCreateSheet) {
        CreateListSheet(
            onDismiss = { showCreateSheet = false },
            onCreate = { name, description, color ->
                showCreateSheet = false
                onCreateList(name, description, color)
            },
        )
    }
}

@Composable
private fun ListsContent(
    lists: List<SharedList>,
    padding: PaddingValues,
    onOpenList: (listId: String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier.widthIn(max = 760.dp).fillMaxSize(),
            contentPadding =
                PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 96.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(lists, key = { it.id }) { list ->
                ListCard(
                    list = list,
                    onClick = { onOpenList(list.id) },
                    modifier = Modifier.testTag("list-${list.id}"),
                )
            }
            if (lists.isEmpty()) {
                item {
                    EmptyState(
                        title = stringResource(R.string.empty_lists),
                        description = stringResource(R.string.empty_lists_description),
                    )
                }
            }
        }
    }
}

@Preview(name = "Téléphone", widthDp = 393, heightDp = 852, showBackground = true)
@Preview(name = "Tablette", widthDp = 1000, heightDp = 800, showBackground = true)
@Preview(
    name = "Sombre",
    widthDp = 393,
    heightDp = 852,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun ListsScreenPreview() {
    TaskMateTheme {
        ListsScreen(
            uiState = ListsUiState.Success(PreviewData.lists),
            onOpenList = {},
            onCreateList = { _, _, _ -> },
            onJoinList = {},
        )
    }
}

@Preview(name = "Vide", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun ListsScreenEmptyPreview() {
    TaskMateTheme {
        ListsScreen(
            uiState = ListsUiState.Success(emptyList()),
            onOpenList = {},
            onCreateList = { _, _, _ -> },
            onJoinList = {},
        )
    }
}

@Preview(name = "Chargement", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun ListsScreenLoadingPreview() {
    TaskMateTheme {
        ListsScreen(
            uiState = ListsUiState.Loading,
            onOpenList = {},
            onCreateList = { _, _, _ -> },
            onJoinList = {},
        )
    }
}
