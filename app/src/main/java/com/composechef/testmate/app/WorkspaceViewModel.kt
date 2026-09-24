package com.composechef.testmate.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.composechef.testmate.core.model.Member
import com.composechef.testmate.core.model.SharedCollection
import com.composechef.testmate.core.model.Task
import com.composechef.testmate.data.demo.DemoWorkspace
import java.util.UUID

/** Session-only data for the UI prototype. No credentials are stored or transmitted. */
class WorkspaceViewModel : ViewModel() {
    var name by mutableStateOf("Alex")
        private set

    var email by mutableStateOf("alex@example.com")
        private set

    val collections = mutableStateListOf<SharedCollection>()
    val tasks = mutableStateListOf<Task>()
    val me
        get() = Member("me", name, name.take(2).uppercase())

    private var sessionLanguage = "fr"

    fun startSession(displayName: String, address: String, demo: Boolean, language: String = "fr") {
        sessionLanguage = language
        name = displayName.trim().ifBlank { "Alex" }
        email = address.trim().ifBlank { "alex@example.com" }
        collections.clear()
        tasks.clear()
        if (demo) {
            collections.addAll(DemoWorkspace.collections(me, sessionLanguage))
            tasks.addAll(DemoWorkspace.tasks(sessionLanguage))
        }
    }

    fun createCollection(
        title: String,
        description: String,
        color: Int,
        symbol: String,
    ): SharedCollection {
        val collection =
            SharedCollection(
                UUID.randomUUID().toString(),
                title.trim(),
                description.trim(),
                color,
                symbol,
                UUID.randomUUID().toString().take(6).uppercase(),
                listOf(me),
            )
        collections.add(collection)
        return collection
    }

    fun joinDemo(language: String = sessionLanguage): SharedCollection =
        collections.find { it.code == "TOGETH" }
            ?: DemoWorkspace.invitedCollection(me, language).also { collections.add(it) }

    fun saveTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index < 0) tasks.add(task) else tasks[index] = task
    }

    fun toggleTask(id: String) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index >= 0) tasks[index] = tasks[index].copy(completed = !tasks[index].completed)
    }

    fun deleteTask(id: String): Task? {
        val task = tasks.find { it.id == id } ?: return null
        tasks.remove(task)
        return task
    }

    fun restoreTask(task: Task, index: Int) {
        if (collections.none { it.id == task.collectionId } || tasks.any { it.id == task.id })
            return
        tasks.add(index.coerceIn(0, tasks.size), task)
    }

    fun deleteCollection(id: String) {
        tasks.removeAll { it.collectionId == id }
        collections.removeAll { it.id == id }
    }

    fun endSession() {
        collections.clear()
        tasks.clear()
    }
}
