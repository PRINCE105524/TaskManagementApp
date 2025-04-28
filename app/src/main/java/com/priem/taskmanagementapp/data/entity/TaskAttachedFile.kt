package com.priem.taskmanagementapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_attached_files")
data class TaskAttachedFile(
    @PrimaryKey(autoGenerate = true)
    val fileId: Long = 0,
    val taskId: Long,
    val fileName: String,
    val filePath: String,
    val fileSize: Long,
    val fileType: String
)
