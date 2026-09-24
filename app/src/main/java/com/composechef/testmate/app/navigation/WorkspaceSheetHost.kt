package com.composechef.testmate.app.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import com.composechef.testmate.app.WorkspaceViewModel
import com.composechef.testmate.feature.collections.CreateCollectionSheet
import com.composechef.testmate.feature.collections.InviteSheet
import com.composechef.testmate.feature.collections.JoinCollectionSheet
import com.composechef.testmate.feature.collections.MembersSheet
import com.composechef.testmate.feature.tasks.TaskEditorSheet

@Composable
internal fun WorkspaceSheetHost(
    sheet: Sheet?,
    state: WorkspaceViewModel,
    onDismiss: () -> Unit,
    openCollection: (String) -> Unit,
    onDeleteTask: (String) -> Unit,
) {
    val language = LocalConfiguration.current.locales[0].language
    when (val active = sheet) {
        Sheet.Create ->
            CreateCollectionSheet({ onDismiss() }) { name, description, color, symbol ->
                val collection = state.createCollection(name, description, color, symbol)
                onDismiss()
                openCollection(collection.id)
            }
        Sheet.Join ->
            JoinCollectionSheet({ onDismiss() }) {
                val collection = state.joinDemo(language)
                onDismiss()
                openCollection(collection.id)
            }
        is Sheet.EditTask ->
            state.collections
                .find { it.id == active.task.collectionId }
                ?.let { collection ->
                    TaskEditorSheet(
                        active.task,
                        collection,
                        { onDismiss() },
                        {
                            state.saveTask(it)
                            onDismiss()
                        },
                        active.isNew,
                        {
                            onDismiss()
                            onDeleteTask(active.task.id)
                        },
                    )
                }
        is Sheet.People ->
            state.collections
                .find { it.id == active.collectionId }
                ?.let {
                    if (active.invite) InviteSheet(it, onDismiss) else MembersSheet(it, onDismiss)
                }
        null -> Unit
    }
}
