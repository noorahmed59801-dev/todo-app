package com.example.todo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.data.Task
import com.example.todo.data.TaskDao

@Database(entities = [Task::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}