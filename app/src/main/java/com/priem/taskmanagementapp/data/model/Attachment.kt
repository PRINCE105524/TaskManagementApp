package com.priem.taskmanagementapp.data.model

data class Attachment(
    val fileName: String,
    val filePath: String,
    val fileSize: Long,
    val fileType: String
)
