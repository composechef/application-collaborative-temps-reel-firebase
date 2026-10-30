package com.composechef.taskmate.data

import com.composechef.taskmate.core.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(listId: String): Flow<List<Task>>

    suspend fun saveTask(task: Task)

    suspend fun setCompleted(task: Task, completed: Boolean)

    suspend fun deleteTask(task: Task)

    fun observeAssignedTasks(userId: String): Flow<List<Task>>
}