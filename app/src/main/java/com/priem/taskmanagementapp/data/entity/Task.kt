package com.priem.taskmanagementapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Long = 0,
    val messageId: Long? = 0,
    val title: String,
    val description: String,
    val priority: String,
    val dueTimestamp: Long?,
    val createdAt: Long = System.currentTimeMillis()
)
