package com.composechef.testmate.core.model

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val collectionId: String,
    val title: String = "",
    val notes: String = "",
    val assignee: String? = null,
    val due: DueDate = DueDate.None,
    val completed: Boolean = false,
)
