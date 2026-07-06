package com.example.todo.presentation.task.completedtasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.todo.data.Task
import com.example.todo.data.TaskRepository
import com.example.todo.presentation.task.incompletedtasklist.SortOption
import com.example.todo.presentation.task.incompletedtasklist.TaskAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompletedTaskListViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val allTasks: LiveData<List<Task>> = repository.getCompletedTasks().asLiveData()

    private val _currentSortOption = MutableLiveData(SortOption.NONE)
    val currentSortOption: LiveData<SortOption> = _currentSortOption

    fun setSortOption(option: SortOption) {
        _currentSortOption.value = option
    }
}