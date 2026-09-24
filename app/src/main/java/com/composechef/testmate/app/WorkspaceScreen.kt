package com.composechef.testmate.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R
import com.composechef.testmate.app.navigation.Destination
import com.composechef.testmate.app.navigation.Sheet
import com.composechef.testmate.core.model.SharedCollection
import com.composechef.testmate.core.model.Task
import com.composechef.testmate.feature.collections.CollectionScreen
import com.composechef.testmate.feature.collections.CollectionsScreen
import com.composechef.testmate.feature.profile.ProfileScreen
import com.composechef.testmate.feature.tasks.MyTasksScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WorkspaceScreen(
    state: WorkspaceViewModel,
    collection: SharedCollection?,
    tab: Destination,
    snackbar: SnackbarHostState,
    goBack: () -> Unit,
    openCollection: (String) -> Unit,
    toggle: (String) -> Unit,
    onSelectTab: (Destination) -> Unit,
    onShowSheet: (Sheet) -> Unit,
    onSignOut: () -> Unit,
    onDeleteTask: (String) -> Unit,
    onDeleteCollection: (String) -> Unit,
) {
    var collectionMenu by remember(collection?.id) { mutableStateOf(false) }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val wide = maxWidth >= 700.dp
        Row {
            if (wide)
                NavigationRail(Modifier.fillMaxHeight().safeDrawingPadding()) {
                    Spacer(Modifier.height(24.dp))
                    Destination.entries.forEach { destination ->
                        NavigationRailItem(
                            selected = tab == destination,
                            onClick = {
                                onSelectTab(destination)
                            },
                            icon = {
                                Icon(destination.icon, stringResource(destination.label))
                            },
                            label = { Text(stringResource(destination.label)) },
                        )
                    }
                }
            Scaffold(
                modifier = Modifier.weight(1f),
                snackbarHost = { SnackbarHost(snackbar) },
                topBar = {
                    if (collection != null)
                        TopAppBar(
                            title = {
                                Text(
                                    collection.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = goBack) {
                                    Icon(
                                        Icons.AutoMirrored.Outlined.ArrowBack,
                                        stringResource(R.string.back_collections),
                                    )
                                }
                            },
                            actions = {
                                Box {
                                    IconButton(onClick = { collectionMenu = true }) {
                                        Icon(
                                            Icons.Outlined.MoreVert,
                                            stringResource(R.string.collection_options),
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = collectionMenu,
                                        onDismissRequest = { collectionMenu = false },
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(stringResource(R.string.delete_collection))
                                            },
                                            onClick = {
                                                collectionMenu = false
                                                onDeleteCollection(collection.id)
                                            },
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        onShowSheet(Sheet.People(collection.id, true))
                                    }
                                ) {
                                    Icon(
                                        Icons.Outlined.PersonAdd,
                                        stringResource(R.string.invite_members),
                                    )
                                }
                            },
                        )
                    else if (tab == Destination.Collections) {
                        TopAppBar(
                            title = { Text(stringResource(R.string.collections)) },
                            actions = {
                                TextButton(onClick = { onShowSheet(Sheet.Join) }) {
                                    Text(stringResource(R.string.join_code))
                                }
                            },
                        )
                    }
                },
                bottomBar = {
                    if (!wide && collection == null)
                        NavigationBar {
                            Destination.entries.forEach { destination ->
                                NavigationBarItem(
                                    selected = tab == destination,
                                    onClick = { onSelectTab(destination) },
                                    icon = { Icon(destination.icon, null) },
                                    label = { Text(stringResource(destination.label)) },
                                )
                            }
                        }
                },
                floatingActionButton = {
                    if (collection != null)
                        ExtendedFloatingActionButton(
                            onClick = {
                                onShowSheet(
                                    Sheet.EditTask(Task(collectionId = collection.id), true)
                                )
                            },
                            icon = { Icon(Icons.Outlined.Add, null) },
                            text = { Text(stringResource(R.string.add_task)) },
                        )
                    else if (tab == Destination.Collections) {
                        ExtendedFloatingActionButton(
                            onClick = { onShowSheet(Sheet.Create) },
                            icon = { Icon(Icons.Outlined.Add, null) },
                            text = { Text(stringResource(R.string.new_collection)) },
                        )
                    }
                },
            ) { padding ->
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Box(Modifier.widthIn(max = 760.dp).fillMaxSize()) {
                        if (collection != null)
                            CollectionScreen(
                                collection,
                                state.tasks.filter {
                                    it.collectionId == collection.id
                                },
                                toggle,
                                { onShowSheet(Sheet.EditTask(it)) },
                                onDeleteTask,
                                { onShowSheet(Sheet.People(collection.id, false)) },
                            )
                        else
                            when (tab) {
                                Destination.Collections ->
                                    CollectionsScreen(
                                        state.collections,
                                        state.tasks,
                                        openCollection,
                                        onDeleteCollection,
                                    )
                                Destination.Tasks ->
                                    MyTasksScreen(
                                        state.tasks,
                                        state.collections,
                                        toggle,
                                        { onShowSheet(Sheet.EditTask(it)) },
                                        onDeleteTask,
                                    )
                                Destination.Profile ->
                                    ProfileScreen(state.name, state.email) {
                                        onSignOut()
                                    }
                            }
                    }
                }
            }
        }
    }
}
