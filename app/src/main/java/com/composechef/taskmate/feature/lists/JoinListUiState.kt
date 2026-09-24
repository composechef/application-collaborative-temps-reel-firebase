package com.composechef.taskmate.feature.lists

import com.composechef.taskmate.core.model.ListInvite

sealed interface JoinListUiState {
    data object Idle : JoinListUiState

    data object Searching : JoinListUiState

    data object NotFound : JoinListUiState

    /** Le code correspond à une liste. [isJoining] passe à `true` pendant qu'on la rejoint. */
    data class Found(val invite: ListInvite, val isJoining: Boolean = false) : JoinListUiState

    data class Joined(val listId: String) : JoinListUiState
}
