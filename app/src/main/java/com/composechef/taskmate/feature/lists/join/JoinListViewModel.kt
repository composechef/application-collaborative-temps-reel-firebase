package com.composechef.taskmate.feature.lists.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composechef.taskmate.core.model.ListInvite
import com.composechef.taskmate.data.AuthRepository
import com.composechef.taskmate.data.ListRepository
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val listRepository: ListRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<JoinListUiState>(JoinListUiState.Idle)
    val uiState: StateFlow<JoinListUiState> = _uiState.asStateFlow()

    fun search(code: String) {
        viewModelScope.launch {
            _uiState.value = JoinListUiState.Searching
            _uiState.value = try {
                listRepository.findInvite(code)?.let { JoinListUiState.Found(it) }
                    ?: JoinListUiState.NotFound
            } catch (e: FirebaseException) {
                JoinListUiState.NotFound
            }
        }
    }

    fun join(invite: ListInvite) {
        val found = _uiState.value as? JoinListUiState.Found ?: return
        _uiState.value = found.copy(isJoining = true)
        viewModelScope.launch {
            _uiState.value = try {
                val user = authRepository.currentUser.first() ?: return@launch
                listRepository.joinList(invite.listId, user)
                JoinListUiState.Joined(invite.listId)
            } catch (e: FirebaseException) {
                found
            }
        }
    }

    fun reset() {
        _uiState.value = JoinListUiState.Idle
    }
}
