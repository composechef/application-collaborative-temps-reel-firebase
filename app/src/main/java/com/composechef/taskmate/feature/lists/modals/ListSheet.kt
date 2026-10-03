package com.composechef.taskmate.feature.lists.modals

import com.composechef.taskmate.core.model.Task

/** Les sheets que l'écran d'une liste peut ouvrir. Une seule est visible à la fois. */
internal sealed interface ListSheet {
    data object Invite : ListSheet

    data object Members : ListSheet

    data class EditTask(val task: Task, val isNew: Boolean) : ListSheet
}