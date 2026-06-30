package com.example.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val time: String,
    val isCompleted: Boolean = false,
    val date: String,
    val description: String = "",
    val isPinned: Boolean = false
)