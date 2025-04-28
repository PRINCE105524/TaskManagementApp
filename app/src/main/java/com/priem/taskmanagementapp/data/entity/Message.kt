package com.priem.taskmanagementapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true)
    val messageId: Long = 0,
    val chatId: Long = 0,
    val senderId: Long = 0,
    val content: String,
    val timestamp: Long,
    val contentType: String = "TEXT", // TEXT or TASK
    val contentJson: String? = null   // for storing task json
)

