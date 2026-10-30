package com.composechef.taskmate.feature.tasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composechef.taskmate.core.coroutines.launchWrite
import com.composechef.taskmate.core.model.Task
import com.composechef.taskmate.data.AuthRepository
import com.composechef.taskmate.data.ListRepository
import com.composechef.taskmate.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MyTasksViewModel @Inject constructor(
    authRepository: AuthRepository,
    listRepository: ListRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    val uiState: StateFlow<MyTasksUiState> = authRepository.currentUser
        .filterNotNull()
        .flatMapLatest { user ->
            combine(
                taskRepository.observeAssignedTasks(user.id),
                listRepository.observeLists(user.id),
            ) { tasks, lists ->
                val listsById = lists.associateBy { it.id }
                val assigned = tasks.mapNotNull { task ->
                    listsById[task.listId]?.let { list -> AssignedTask(task, list) }
                }
                MyTasksUiState.Success(assigned)
            }
        }
        .catch { error ->
            Log.w("TaskMate", "Impossible d'écouter les tâches attribuées", error)
            emit(MyTasksUiState.Success(emptyList()))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MyTasksUiState.Loading,
        )

    fun setTaskCompleted(task: Task, completed: Boolean) = launchWrite {
        taskRepository.setCompleted(task, completed)
    }

    fun deleteTask(task: Task) = launchWrite { taskRepository.deleteTask(task) }

    fun restoreTask(task: Task) = launchWrite { taskRepository.saveTask(task) }
}
