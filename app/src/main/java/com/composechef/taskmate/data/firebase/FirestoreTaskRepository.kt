package com.composechef.taskmate.data.firebase

import com.composechef.taskmate.core.model.DueDate
import com.composechef.taskmate.core.model.Task
import com.composechef.taskmate.data.TaskRepository
import com.composechef.taskmate.data.firebase.model.TaskDocument
import com.composechef.taskmate.data.firebase.model.toDomain
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.composechef.taskmate.data.firebase.model.toDocument
import kotlinx.coroutines.tasks.await


class FirestoreTaskRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : TaskRepository {

    private fun tasksOf(listId: String): CollectionReference = firestore
        .collection("lists")
        .document(listId)
        .collection("tasks")

    override fun observeTasks(listId: String): Flow<List<Task>> {
        return tasksOf(listId).dataObjects<TaskDocument>().map { documents ->
            documents.map { it.toDomain() }
        }
    }

    override suspend fun saveTask(task: Task) {
        tasksOf(task.listId).document(task.id).set(task.toDocument()).await()
    }

    override suspend fun setCompleted(task: Task, completed: Boolean) {
        tasksOf(task.listId).document(task.id).update("completed", completed).await()
    }

    override suspend fun deleteTask(task: Task) {
        tasksOf(task.listId).document(task.id).delete().await()
    }

    override fun observeAssignedTasks(userId: String): Flow<List<Task>> {
        return firestore
            .collectionGroup("tasks")
            .whereEqualTo("assigneeId", userId)
            .dataObjects<TaskDocument>()
            .map { documents -> documents.map { it.toDomain() }.sortedWith(TaskOrder) }
    }


    private companion object {
        /** Les tâches les plus urgentes d'abord, celles sans échéance à la fin, puis par titre. */
        val TaskOrder = compareBy<Task>(
            { if (it.due == DueDate.None) Int.MAX_VALUE else it.due.ordinal },
            { it.title.lowercase() },
        )
    }
}
