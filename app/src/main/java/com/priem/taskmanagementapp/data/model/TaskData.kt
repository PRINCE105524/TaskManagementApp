package com.priem.taskmanagementapp.data.model

data class TaskData(
    val taskId: Long,
    val messageId: Long?,
    val title: String,
    val description: String,
    val priority: String,
    val dueTimestamp: Long?,
    val labels: List<String>?,
    val followers: List<FollowerData>?,
    val attachedMessages: List<Long>?,
    val attachedFiles: List<FileData>?
)

data class FollowerData(
    val userId: Long,
    val name: String,
    val avatarUrl: String? = null
)


data class FileData(
    val fileId: Long,
    val fileName: String,
    val fileUrl: String,
    val fileSize: Long,
    val fileType: String
)
