package com.example.todo.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val dao: TaskDao
) {
    fun getIncompleteTasks(): Flow<List<Task>> = dao.getIncompleteTasks()
    fun getCompletedTasks(): Flow<List<Task>> = dao.getCompletedTasks()
    fun getTodayIncompleteTasks(today: String): Flow<List<Task>> = dao.getTodayIncompleteTasks(today)
    fun getTodayCompletedTasks(today: String): Flow<List<Task>> = dao.getTodayCompletedTasks(today)
    suspend fun getTaskById(taskId: Int): Task? = dao.getTaskById(taskId)
    suspend fun insertTask(task: Task) = dao.insertTask(task)
    suspend fun updateTask(task: Task) = dao.updateTask(task)
    suspend fun deleteTask(task: Task) = dao.deleteTask(task)
}