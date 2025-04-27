package com.priem.taskmanagementapp.repository


import androidx.lifecycle.LiveData
import com.priem.taskmanagementapp.data.dao.TaskDao
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.data.entity.Task
import com.priem.taskmanagementapp.data.entity.TaskLabelCrossRef
import com.priem.taskmanagementapp.data.relation.TaskWithLabels

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    // Labels related methods
    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    suspend fun insertLabelAndReturnId(label: Label): Long {
        return taskDao.insertLabelAndReturnId(label)
    }

    suspend fun getLabelIdByName(labelName: String): Long? {
        return taskDao.getLabelIdByName(labelName)
    }

    suspend fun insertTaskLabelCrossRef(crossRef: TaskLabelCrossRef) {
        taskDao.insertTaskLabelCrossRef(crossRef)
    }


}
