package com.composechef.taskmate.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.composechef.taskmate.core.designsystem.theme.TaskMateTheme

/**
 * La coque des écrans principaux : une barre de navigation en bas sur téléphone, un rail à gauche
 * quand la fenêtre est large. Le choix suit la classe de taille de la fenêtre, calculée par
 * [NavigationSuiteScaffold]. Elle affiche l'onglet sélectionné et prévient quand on en choisit un
 * autre, sans savoir comment la navigation fonctionne.
 *
 * @param showNavigation `false` pour masquer la barre, par exemple sur l'écran d'une liste.
 */
@Composable
fun HomeScaffold(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    modifier: Modifier = Modifier,
    showNavigation: Boolean = true,
    content: @Composable () -> Unit,
) {
    val layoutType = if (showNavigation) {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
    } else {
        NavigationSuiteType.None
    }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            HomeTab.entries.forEach { tab ->
                item(
                    selected = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                    icon = { Icon(imageVector = tab.icon, contentDescription = null) },
                    label = { Text(text = stringResource(tab.label)) },
                )
            }
        },
        modifier = modifier.fillMaxSize(),
        layoutType = layoutType,
        content = content,
    )
}

@Preview(name = "Téléphone", device = "spec:width=393dp,height=600dp", showBackground = true)
@Preview(name = "Tablette", device = "spec:width=1000dp,height=600dp", showBackground = true)
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
