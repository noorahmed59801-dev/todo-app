package com.example.todo.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.todo.data.Task
import com.example.todo.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
@Suppress("UnusedPrivateProperty")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private fun getToday(): String {
        return SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(Date())
    }

    val incompleteTasks: LiveData<List<Task>> =
        repository.getIncompleteTasks().asLiveData()

    val completedTasks: LiveData<List<Task>> =
        repository.getCompletedTasks().asLiveData()

    val todayCompletedCount: LiveData<Int> =
        repository.getTodayCompletedTasks(getToday())
            .map { tasks -> tasks.size }
            .asLiveData()

    val todayRemainingCount: LiveData<Int> =
        repository.getTodayIncompleteTasks(getToday())
            .map { tasks -> tasks.size }
            .asLiveData()
}