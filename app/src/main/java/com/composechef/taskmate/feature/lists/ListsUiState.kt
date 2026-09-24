package com.composechef.taskmate.feature.lists

import com.composechef.taskmate.core.model.SharedList

sealed interface ListsUiState {
    data object Loading : ListsUiState

    data class Success(val lists: List<SharedList>) : ListsUiState
}
