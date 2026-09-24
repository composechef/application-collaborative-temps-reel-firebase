package com.composechef.taskmate.feature.tasks

sealed interface MyTasksUiState {
    data object Loading : MyTasksUiState

    data class Success(val tasks: List<AssignedTask>) : MyTasksUiState
}
