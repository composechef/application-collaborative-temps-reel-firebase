package com.composechef.testmate

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.os.LocaleListCompat
import org.junit.Rule
import org.junit.Test

class LanguageTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun switchingLanguagePreservesWorkspaceAndSelectionAfterRecreation() {
        compose.runOnUiThread {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("fr"))
        }
        compose.onNodeWithText("Essayer la démo").performClick()
        compose.onNodeWithText("Lancement du studio").assertIsDisplayed()
        compose.onNodeWithText("Profil").performClick()
        compose.onNodeWithText("Français").performClick()
        compose.onNodeWithText("English").performClick()
        compose.onNodeWithText("Sign out").assertIsDisplayed()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("Sign out").assertIsDisplayed()
        compose.onNodeWithText("Collections").performClick()
        // Existing collection names are content, not interface labels.
        compose.onNodeWithText("Lancement du studio").assertIsDisplayed()
        compose.onNodeWithText("Join with code").assertIsDisplayed()
        compose.onNodeWithText("Profile").performClick()
        compose.onNodeWithText("English").performClick()
        compose.onNodeWithText("Français").performClick()
        compose.onNodeWithText("Se déconnecter").assertIsDisplayed()
    }
}
