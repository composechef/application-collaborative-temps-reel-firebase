package com.composechef.testmate.app.navigation

import com.composechef.testmate.core.model.Task

internal sealed interface Sheet {
    data object Create : Sheet

    data object Join : Sheet

    data class EditTask(val task: Task, val isNew: Boolean = false) : Sheet

    data class People(val collectionId: String, val invite: Boolean) : Sheet
}
