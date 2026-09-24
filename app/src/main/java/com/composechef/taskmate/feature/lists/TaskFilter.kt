package com.composechef.taskmate.feature.lists

import androidx.annotation.StringRes
import com.composechef.taskmate.R
import com.composechef.taskmate.core.model.Task

/** Les filtres proposés sur l'écran d'une liste, avec le message affiché quand rien ne correspond. */
internal enum class TaskFilter(
    @StringRes val label: Int,
    @StringRes val emptyTitle: Int,
    @StringRes val emptyDescription: Int,
) {
    ToDo(R.string.to_do, R.string.empty_to_do, R.string.empty_to_do_description),
    Mine(R.string.mine, R.string.empty_mine, R.string.empty_mine_description),
    Completed(R.string.completed, R.string.empty_completed, R.string.empty_completed_description);

    fun matches(task: Task, currentUserId: String): Boolean =
        when (this) {
            ToDo -> !task.completed
            Mine -> task.assigneeId == currentUserId && !task.completed
            Completed -> task.completed
        }
}
