package com.priem.taskmanagementapp.data.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.data.entity.Task
import com.priem.taskmanagementapp.data.entity.TaskLabelCrossRef

data class TaskWithLabels(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "labelId",
        associateBy = Junction(TaskLabelCrossRef::class)
    )
    val labels: List<Label>
)
