package com.composechef.taskmate.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.composechef.taskmate.core.designsystem.components.HomeScaffold
import com.composechef.taskmate.core.designsystem.components.HomeTab
import com.composechef.taskmate.core.model.User
import com.composechef.taskmate.feature.lists.ListsRoute
import com.composechef.taskmate.feature.lists.details.ListDetailRoute
import com.composechef.taskmate.feature.profile.ProfileRoute
import com.composechef.taskmate.feature.tasks.MyTasksRoute


@Composable
fun MainNavigation(user: User) {
    val backStack = rememberNavBackStack(Lists)
    val selectedTab = backStack.firstOrNull().toHomeTab()

    HomeScaffold(
        selectedTab = selectedTab,
        onTabSelected = { tab -> backStack.switchTab(tab.toNavKey()) },
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<Lists> {
                    ListsRoute(
                        onOpenList = { listId ->
                            backStack.add(ListDetail(listId))
                        }
                    )
                }
                entry<MyTasks> {
                    MyTasksRoute(
                        onOpenList = { listId ->
                            backStack.add(ListDetail(listId))
                        }
                    )
                }
                entry<Profile> {
                    ProfileRoute(user = user)
                }
                entry<ListDetail> { key ->
                    ListDetailRoute(
                        listId = key.listId,
                        onBack = { backStack.removeLastOrNull() },
                    )
                }
            },
        )
    }
}

private fun NavKey?.toHomeTab(): HomeTab = when (this) {
    MyTasks -> HomeTab.MyTasks
    Profile -> HomeTab.Profile
    else -> HomeTab.Lists
}

private fun HomeTab.toNavKey(): NavKey = when (this) {
    HomeTab.Lists -> Lists
    HomeTab.MyTasks -> MyTasks
    HomeTab.Profile -> Profile
}
