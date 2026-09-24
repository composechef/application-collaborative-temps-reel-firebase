package com.composechef.taskmate.core.model

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val listId: String,
    val title: String = "",
    val notes: String = "",
    val assigneeId: String? = null,
    val due: DueDate = DueDate.None,
    val completed: Boolean = false,
)
