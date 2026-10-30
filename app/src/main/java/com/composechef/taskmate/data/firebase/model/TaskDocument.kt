package com.composechef.taskmate.data.firebase.model

import com.composechef.taskmate.core.model.DueDate
import com.composechef.taskmate.core.model.Task
import com.google.firebase.firestore.DocumentId

data class TaskDocument(
    @DocumentId val id: String = "",
    val listId: String = "",
    val title: String = "",
    val notes: String = "",
    val assigneeId: String? = null,
    val due: DueDate = DueDate.None,
    val completed: Boolean = false,
)

fun TaskDocument.toDomain(): Task {
    return Task(
        id = id,
        listId = listId,
        title = title,
        notes = notes,
        assigneeId = assigneeId,
        due = due,
        completed = completed,
    )
}

fun Task.toDocument(): TaskDocument {
    return TaskDocument(
        id = id,
        listId = listId,
        title = title,
        notes = notes,
        assigneeId = assigneeId,
        due = due,
        completed = completed,
    )
}
