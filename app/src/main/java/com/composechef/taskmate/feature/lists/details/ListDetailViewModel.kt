package com.composechef.taskmate.feature.lists.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composechef.taskmate.data.AuthRepository
import com.composechef.taskmate.data.ListRepository
import com.composechef.taskmate.data.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import com.composechef.taskmate.core.coroutines.launchWrite
import com.composechef.taskmate.core.model.Task

@HiltViewModel(assistedFactory = ListDetailViewModel.Factory::class)
class ListDetailViewModel @AssistedInject constructor(
    @Assisted private val listId: String,
    authRepository: AuthRepository,
    private val listRepository: ListRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    val uiState: StateFlow<ListDetailUiState> = combine(
        authRepository.currentUser.filterNotNull(),
        listRepository.observeList(listId),
        taskRepository.observeTasks(listId),
    ) { user, list, tasks ->
        if (list == null) {
            ListDetailUiState.NotFound
        } else {
            ListDetailUiState.Success(list = list, tasks = tasks, currentUserId = user.id)
        }
    }.catch { error ->
        Log.w("TaskMate", "Impossible d'écouter la liste $listId", error)
        emit(ListDetailUiState.NotFound)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ListDetailUiState.Loading,
    )

    fun saveTask(task: Task) = launchWrite { taskRepository.saveTask(task) }

    fun setTaskCompleted(task: Task, completed: Boolean) = launchWrite {
        taskRepository.setCompleted(task, completed)
    }

    fun deleteTask(task: Task) = launchWrite { taskRepository.deleteTask(task) }

    fun deleteList() {
        val state = uiState.value as? ListDetailUiState.Success ?: return
        launchWrite { listRepository.deleteList(state.list) }
    }

    @AssistedFactory
    interface Factory {
        fun create(listId: String): ListDetailViewModel
    }
}
