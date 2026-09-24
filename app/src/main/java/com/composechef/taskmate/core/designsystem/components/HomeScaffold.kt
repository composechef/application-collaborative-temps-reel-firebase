package com.composechef.taskmate.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.core.designsystem.theme.TaskMateTheme

/**
 * La coque des écrans principaux : une barre de navigation en bas sur téléphone, un rail à gauche
 * quand la fenêtre est large. Elle affiche l'onglet sélectionné et prévient quand on en choisit un
 * autre, sans savoir comment la navigation fonctionne.
 *
 * @param showNavigation `false` pour masquer la barre, par exemple sur l'écran d'une liste.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScaffold(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    modifier: Modifier = Modifier,
    showNavigation: Boolean = true,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val wide = maxWidth >= 700.dp
        val showRail = showNavigation && wide
        val showBottomBar = showNavigation && !wide
        Row(modifier = Modifier.fillMaxSize()) {
            if (showRail) {
                NavigationRail(modifier = Modifier.fillMaxHeight()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    HomeTab.entries.forEach { tab ->
                        NavigationRailItem(
                            selected = tab == selectedTab,
                            onClick = { onTabSelected(tab) },
                            icon = { Icon(imageVector = tab.icon, contentDescription = null) },
                            label = { Text(text = stringResource(tab.label)) },
                        )
                    }
                }
            }
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                Box(
                    modifier =
                        Modifier.weight(1f)
                            .fillMaxWidth()
                            .then(
                                when {
                                    showBottomBar ->
                                        Modifier.consumeWindowInsets(WindowInsets.navigationBars)
                                    showRail ->
                                        Modifier.consumeWindowInsets(
                                            WindowInsets.safeDrawing.only(WindowInsetsSides.Start)
                                        )
                                    else -> Modifier
                                }
                            )
                ) {
                    content()
                }
                if (showBottomBar) {
                    NavigationBar {
                        HomeTab.entries.forEach { tab ->
                            NavigationBarItem(
                                selected = tab == selectedTab,
                                onClick = { onTabSelected(tab) },
                                icon = { Icon(imageVector = tab.icon, contentDescription = null) },
                                label = { Text(text = stringResource(tab.label)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Téléphone", widthDp = 393, heightDp = 600, showBackground = true)
@Preview(name = "Tablette", widthDp = 1000, heightDp = 600, showBackground = true)
@Composable
private fun HomeScaffoldPreview() {
    TaskMateTheme {
        HomeScaffold(selectedTab = HomeTab.Lists, onTabSelected = {}) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Contenu de l'onglet",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}
