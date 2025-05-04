package com.priem.taskmanagementapp.data.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "task_labels",
    primaryKeys = ["taskId", "labelId"],
    indices = [Index(value = ["labelId"])]
)
data class TaskLabelCrossRef(
    val taskId: Long,
    val labelId: Long
)
