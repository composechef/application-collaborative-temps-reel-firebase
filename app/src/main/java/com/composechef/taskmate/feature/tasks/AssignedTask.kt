package com.composechef.taskmate.feature.tasks

import com.composechef.taskmate.core.model.SharedList
import com.composechef.taskmate.core.model.Task

/** Une tâche attribuée à l'utilisateur, avec la liste à laquelle elle appartient. */
data class AssignedTask(val task: Task, val list: SharedList)
