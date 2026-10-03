package com.composechef.taskmate.feature.lists.details

import com.composechef.taskmate.core.model.SharedList
import com.composechef.taskmate.core.model.Task

sealed interface ListDetailUiState {
    data object Loading : ListDetailUiState

    /** La liste n'existe pas ou plus, par exemple parce que son propriétaire l'a supprimée. */
    data object NotFound : ListDetailUiState

    data class Success(
        val list: SharedList,
        val tasks: List<Task>,
        val currentUserId: String,
    ) : ListDetailUiState
}