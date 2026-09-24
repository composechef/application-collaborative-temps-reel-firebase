package com.composechef.testmate.feature.collections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R
import com.composechef.testmate.core.designsystem.components.EmptyState
import com.composechef.testmate.core.designsystem.components.SwipeActions
import com.composechef.testmate.core.model.SharedCollection
import com.composechef.testmate.core.model.Task
import com.composechef.testmate.feature.collections.components.CollectionCard

@Composable
fun CollectionsScreen(
    collections: List<SharedCollection>,
    tasks: List<Task>,
    onOpen: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(collections, key = { it.id }) { collection ->
            SwipeActions(
                modifier = Modifier.testTag("collection-${collection.id}"),
                onDelete = { onDelete(collection.id) },
            ) {
                CollectionCard(collection, tasks.filter { it.collectionId == collection.id }) {
                    onOpen(collection.id)
                }
            }
        }
        if (collections.isEmpty()) {
            item {
                EmptyState(
                    stringResource(R.string.empty_collections),
                    stringResource(R.string.empty_collections_description),
                )
            }
        }
    }
}
