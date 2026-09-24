package com.composechef.testmate.app

import com.composechef.testmate.core.model.Task
import org.junit.Assert.*
import org.junit.Test

class WorkspaceViewModelTest {
    @Test
    fun newSessionDoesNotInheritDemoData() {
        val state = WorkspaceViewModel()
        state.startSession("Alex", "alex@example.com", true)
        assertTrue(state.collections.isNotEmpty())
        state.startSession("Taylor", "taylor@example.com", false)
        assertTrue(state.collections.isEmpty())
        assertTrue(state.tasks.isEmpty())
        assertEquals("Taylor", state.me.name)
    }

    @Test
    fun joiningTheSameCollectionDoesNotDuplicateIt() {
        val state = WorkspaceViewModel()
        val first = state.joinDemo()
        assertEquals(first.id, state.joinDemo().id)
        assertEquals(1, state.collections.size)
    }

    @Test
    fun editingATaskPreservesItsIdentityAndCompletion() {
        val state = WorkspaceViewModel()
        val collection = state.createCollection("  Our plans  ", "", 0, "✦")
        val task = Task(collectionId = collection.id, title = "First draft")
        state.saveTask(task)
        state.toggleTask(task.id)
        state.saveTask(state.tasks.single().copy(title = "Final draft", assignee = "me"))
        assertEquals(1, state.tasks.size)
        assertEquals(task.id, state.tasks.single().id)
        assertTrue(state.tasks.single().completed)
        assertEquals("Final draft", state.tasks.single().title)
        assertEquals("Our plans", collection.name)
    }

    @Test
    fun signOutClearsTheWorkspace() {
        val state = WorkspaceViewModel()
        state.startSession("Alex", "alex@example.com", true)
        state.endSession()
        assertTrue(state.tasks.isEmpty())
        assertTrue(state.collections.isEmpty())
    }

    @Test
    fun deletedTaskCanBeRestoredInItsOriginalPosition() {
        val state = WorkspaceViewModel().apply { startSession("Alex", "alex@example.com", true) }
        val original = state.tasks.toList()
        val deleted = state.deleteTask(original[1].id)!!
        assertFalse(state.tasks.any { it.id == deleted.id })
        state.restoreTask(deleted, 1)
        state.restoreTask(deleted, 1)
        assertEquals(original, state.tasks.toList())
    }

    @Test
    fun deletingCollectionRemovesOnlyItsTasks() {
        val state = WorkspaceViewModel().apply { startSession("Alex", "alex@example.com", true) }
        val remaining = state.tasks.filter { it.collectionId != "launch" }
        state.deleteCollection("launch")
        assertFalse(state.collections.any { it.id == "launch" })
        assertEquals(remaining, state.tasks.toList())
    }

    @Test
    fun undoCannotRestoreTasksIntoDeletedCollections() {
        val state = WorkspaceViewModel().apply { startSession("Alex", "alex@example.com", true) }
        val deleted = state.deleteTask(state.tasks.first().id)!!
        state.deleteCollection(deleted.collectionId)
        state.restoreTask(deleted, 0)
        assertFalse(state.tasks.any { it.id == deleted.id })
    }
}
