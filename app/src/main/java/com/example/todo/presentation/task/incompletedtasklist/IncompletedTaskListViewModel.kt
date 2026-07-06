package com.example.todo.presentation.task.incompletedtasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.todo.data.Task
import com.example.todo.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class SortOption {
    NONE, TITLE_ASC, TITLE_DESC, DATE_ASC, DATE_DESC, COMPLETED, INCOMPLETE
}

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val allTasks: LiveData<List<Task>> = repository.getIncompleteTasks().asLiveData()

    private val _currentSortOption = MutableLiveData(SortOption.NONE)
    val currentSortOption: LiveData<SortOption> = _currentSortOption

    fun setSortOption(option: SortOption) {
        _currentSortOption.value = option
    }
}