package com.composechef.taskmate.feature.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composechef.taskmate.data.AuthRepository
import com.composechef.taskmate.data.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.composechef.taskmate.core.coroutines.launchWrite
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ListsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val listRepository: ListRepository,
) : ViewModel() {

    val uiState: StateFlow<ListsUiState> = authRepository.currentUser
        .filterNotNull()
        .flatMapLatest { user -> listRepository.observeLists(user.id) }
        .map { lists -> ListsUiState.Success(lists) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ListsUiState.Loading,
        )

    fun createList(name: String, description: String, color: Int) = launchWrite {
        val owner = authRepository.currentUser.first() ?: return@launchWrite
        listRepository.createList(owner, name, description, color)
    }
}
