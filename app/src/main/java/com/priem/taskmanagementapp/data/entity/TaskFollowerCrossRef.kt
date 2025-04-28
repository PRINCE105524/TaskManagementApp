package com.priem.taskmanagementapp.data.entity

import androidx.room.Entity

@Entity(primaryKeys = ["taskId", "userId"])
data class TaskFollowerCrossRef(
    val taskId: Long,
    val userId: Long
)
