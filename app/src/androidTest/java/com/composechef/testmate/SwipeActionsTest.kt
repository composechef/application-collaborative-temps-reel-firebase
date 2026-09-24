package com.composechef.testmate

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.composechef.testmate.app.WorkspaceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@org.junit.runner.RunWith(org.junit.runners.Parameterized::class)
class SwipeActionsTest(private val language: String) {
    companion object {
        @JvmStatic
        @org.junit.runners.Parameterized.Parameters(name = "{0}")
        fun languages() = listOf(arrayOf("fr"), arrayOf("en"))
    }

    private val localizedContext by lazy {
        val context =
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        val config = android.content.res.Configuration(context.resources.configuration)
        config.setLocales(android.os.LocaleList.forLanguageTags(language))
        context.createConfigurationContext(config)
    }

    private fun text(id: Int) = localizedContext.getString(id)

    @get:Rule val compose = createAndroidComposeRule<MainActivity>()
    private lateinit var state: WorkspaceViewModel

    @Before
    fun startDemo() {
        compose.runOnUiThread {
            androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(
                androidx.core.os.LocaleListCompat.forLanguageTags(language)
            )
        }
        compose.waitForIdle()
        state =
            androidx.lifecycle.ViewModelProvider(compose.activity)[WorkspaceViewModel::class.java]
        compose.onNodeWithText(text(R.string.try_demo)).performClick()
        compose.waitForIdle()
    }

    @Test
    fun leftSwipeRevealsDeleteAndUndoRestoresTask() {
        compose.onNodeWithText(state.collections.first { it.id == "launch" }.name).performClick()
        val task = state.tasks.first()
        compose.onNodeWithTag("task-${task.id}").performTouchInput { swipeLeft() }
        compose.runOnIdle { assertTrue(state.tasks.any { it.id == task.id }) }
        compose.onNodeWithText(text(R.string.delete)).performClick()
        compose.runOnIdle { assertFalse(state.tasks.any { it.id == task.id }) }
        compose.onNodeWithText(text(R.string.undo)).performClick()
        compose.onNodeWithText(task.title).assertIsDisplayed()
        compose.runOnIdle { assertEquals(task, state.tasks.first()) }
    }

    @Test
    fun rightSwipeCompletesTaskAndUndoRestoresIt() {
        compose.onNodeWithText(state.collections.first { it.id == "launch" }.name).performClick()
        val task = state.tasks.first()
        compose.onNodeWithTag("task-${task.id}").performTouchInput { swipeRight() }
        compose.runOnIdle { assertTrue(state.tasks.first { it.id == task.id }.completed) }
        compose.onNodeWithText(text(R.string.undo)).performClick()
        compose.onNodeWithText(task.title).assertIsDisplayed()
        compose.runOnIdle { assertFalse(state.tasks.first { it.id == task.id }.completed) }
    }

    @Test
    fun collectionSwipeRequiresConfirmationAndCascadesDeletion() {
        compose.onNodeWithTag("collection-launch").performTouchInput { swipeLeft() }
        compose.onNodeWithText(text(R.string.delete)).performClick()
        compose.onNodeWithText(text(R.string.delete_collection_title)).assertIsDisplayed()
        compose.onNodeWithText(text(R.string.cancel)).performClick()
        compose.runOnIdle { assertTrue(state.collections.any { it.id == "launch" }) }
        compose.onNodeWithTag("collection-launch").performTouchInput { swipeLeft() }
        compose.onNodeWithText(text(R.string.delete)).performClick()
        compose.onNodeWithText(text(R.string.delete)).performClick()
        compose.runOnIdle {
            assertFalse(state.collections.any { it.id == "launch" })
            assertFalse(state.tasks.any { it.collectionId == "launch" })
            assertTrue(state.tasks.any { it.collectionId == "home" })
        }
    }

    @Test
    fun detailsAndMenuProvideDeletionWithoutGestures() {
        compose.onNodeWithText(state.collections.first { it.id == "launch" }.name).performClick()
        val task = state.tasks.first()
        compose.onNodeWithText(task.title).performClick()
        compose.onNodeWithText(text(R.string.delete_task)).performScrollTo().performClick()
        compose.runOnIdle { assertFalse(state.tasks.any { it.id == task.id }) }
        compose.onNodeWithText(text(R.string.undo)).performClick()
        compose.onNodeWithContentDescription(text(R.string.collection_options)).performClick()
        compose.onNodeWithText(text(R.string.delete_collection)).performClick()
        compose.onNodeWithText(text(R.string.delete)).performClick()
        compose.onNodeWithText(text(R.string.join_code)).assertIsDisplayed()
        compose.runOnIdle { assertFalse(state.collections.any { it.id == "launch" }) }
    }
}
