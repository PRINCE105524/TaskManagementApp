package com.priem.taskmanagementapp.data.entity

import androidx.room.Entity

@Entity(primaryKeys = ["taskId", "labelId"], tableName = "task_labels")
data class TaskLabelCrossRef(
    val taskId: Long,
    val labelId: Long
)
