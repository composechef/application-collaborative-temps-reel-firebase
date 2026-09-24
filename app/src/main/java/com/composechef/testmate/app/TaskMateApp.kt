package com.composechef.testmate.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.composechef.testmate.R
import com.composechef.testmate.app.navigation.Destination
import com.composechef.testmate.app.navigation.Sheet
import com.composechef.testmate.app.navigation.WorkspaceSheetHost
import com.composechef.testmate.feature.auth.AuthScreen
import com.composechef.testmate.feature.collections.DeleteCollectionDialog
import com.composechef.testmate.feature.profile.SignOutDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskMateApp(state: WorkspaceViewModel = viewModel()) {
    val resources = LocalResources.current
    val language = LocalConfiguration.current.locales[0].language
    var routes by rememberSaveable { mutableStateOf(listOf("welcome")) }
    var tab by rememberSaveable { mutableStateOf(Destination.Collections) }
    var sheet by remember { mutableStateOf<Sheet?>(null) }
    var pendingDeleteCollection by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmSignOut by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val goBack = { if (routes.size > 1) routes = routes.dropLast(1) }
    val openCollection: (String) -> Unit = { routes = listOf("workspace", "collection/$it") }
    val toggle: (String) -> Unit = { id ->
        val wasCompleted = state.tasks.find { it.id == id }?.completed ?: false
        state.toggleTask(id)
        scope.launch {
            snackbar.currentSnackbarData?.dismiss()
            if (
                snackbar.showSnackbar(
                    if (wasCompleted) resources.getString(R.string.task_reopened)
                    else resources.getString(R.string.task_completed),
                    resources.getString(R.string.undo),
                    duration = SnackbarDuration.Short,
                ) == SnackbarResult.ActionPerformed
            ) {
                state.tasks
                    .find { it.id == id }
                    ?.let { state.saveTask(it.copy(completed = wasCompleted)) }
            }
        }
    }
    val deleteTask: (String) -> Unit = { id ->
        val index = state.tasks.indexOfFirst { it.id == id }
        state.deleteTask(id)?.let { deleted ->
            scope.launch {
                snackbar.currentSnackbarData?.dismiss()
                if (
                    snackbar.showSnackbar(
                        resources.getString(R.string.task_deleted),
                        resources.getString(R.string.undo),
                        duration = SnackbarDuration.Long,
                    ) == SnackbarResult.ActionPerformed
                ) {
                    state.restoreTask(deleted, index)
                }
            }
        }
    }
    Surface(Modifier.fillMaxSize()) {
        NavDisplay(
            backStack = routes,
            onBack = goBack,
            entryProvider = { route ->
                NavEntry(route) {
                    if (route != "workspace" && !route.startsWith("collection/")) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                            Box(Modifier.widthIn(max = 520.dp)) {
                                AuthScreen(
                                    route,
                                    { next ->
                                        routes =
                                            if (next == "reset") routes + next
                                            else listOf("welcome", next)
                                    },
                                    goBack,
                                ) { name, email, demo ->
                                    state.startSession(
                                        name,
                                        email,
                                        demo,
                                        language,
                                    )
                                    tab = Destination.Collections
                                    routes = listOf("workspace")
                                }
                            }
                        }
                    } else {
                        val collection =
                            route.substringAfter("collection/", "").let { id ->
                                state.collections.find { it.id == id }
                            }
                        WorkspaceScreen(
                            state = state,
                            collection = collection,
                            tab = tab,
                            snackbar = snackbar,
                            goBack = goBack,
                            openCollection = openCollection,
                            toggle = toggle,
                            onSelectTab = {
                                tab = it
                                routes = listOf("workspace")
                            },
                            onShowSheet = { sheet = it },
                            onSignOut = { confirmSignOut = true },
                            onDeleteTask = deleteTask,
                            onDeleteCollection = { pendingDeleteCollection = it },
                        )
                    }
                }
            },
        )
    }
    WorkspaceSheetHost(sheet, state, { sheet = null }, openCollection, deleteTask)
    state.collections
        .find { it.id == pendingDeleteCollection }
        ?.let { collection ->
            DeleteCollectionDialog(
                name = collection.name,
                onDismiss = { pendingDeleteCollection = null },
                onConfirm = {
                    pendingDeleteCollection = null
                    sheet = null
                    routes = listOf("workspace")
                    tab = Destination.Collections
                    state.deleteCollection(collection.id)
                    scope.launch {
                        snackbar.currentSnackbarData?.dismiss()
                        snackbar.showSnackbar(resources.getString(R.string.collection_deleted))
                    }
                },
            )
        }
    if (confirmSignOut) {
        SignOutDialog(
            onDismiss = { confirmSignOut = false },
            onConfirm = {
                confirmSignOut = false
                routes = listOf("welcome")
                state.endSession()
            },
        )
    }
}
