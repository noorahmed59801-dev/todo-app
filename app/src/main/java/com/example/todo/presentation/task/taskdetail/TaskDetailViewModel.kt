package com.example.todo.presentation.task.taskdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.Task
import com.example.todo.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _selectedTask = MutableLiveData<Task?>()
    val selectedTask: LiveData<Task?> = _selectedTask

    fun loadTaskById(taskId: Int) {
        viewModelScope.launch {
            _selectedTask.postValue(repository.getTaskById(taskId))
        }
    }

    fun markAsDone(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = true))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun togglePin(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isPinned = !task.isPinned))
            loadTaskById(task.id)
        }
    }
}