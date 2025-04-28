package com.priem.taskmanagementapp.repository


import androidx.lifecycle.LiveData
import com.priem.taskmanagementapp.data.dao.TaskDao
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.data.entity.Message
import com.priem.taskmanagementapp.data.entity.Task
import com.priem.taskmanagementapp.data.entity.TaskFollowerCrossRef
import com.priem.taskmanagementapp.data.entity.TaskLabelCrossRef
import com.priem.taskmanagementapp.data.entity.User
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

    // User related methods
    suspend fun insertDummyUsers() {
        val dummyUsers = listOf(
            User(name = "Alice Johnson", phoneNumber = "+8801711001100"),
            User(name = "Bob Smith", phoneNumber = "+8801711002200"),
            User(name = "Charlie Brown", phoneNumber = "+8801711003300"),
            User(name = "David Williams", phoneNumber = "+8801711004400"),
            User(name = "Eve Davis", phoneNumber = "+8801711005500")
        )
        taskDao.insertUsers(dummyUsers)
    }

     fun getAllUsers() : LiveData<List<User>> {
        return taskDao.getAllUsers()
    }

    // TaskFollowerCrossRef related methods
    suspend fun insertTaskFollowerCrossRef(taskId: Long, userId: Long) {
        taskDao.insertTaskFollowerCrossRef(TaskFollowerCrossRef(taskId, userId))
    }

    // Message related methods
    suspend fun insertDummyMessages() {
        val dummyMessages = listOf(
            Message(content = "Hey, are you coming to the meeting?", timestamp = System.currentTimeMillis(), chatId = 1, senderId = 1),
            Message(content = "Don't forget to submit the report.", timestamp = System.currentTimeMillis(), chatId = 1, senderId = 2),
            Message(content = "Here is the design file.", timestamp = System.currentTimeMillis(), chatId = 1, senderId = 3),
            Message(content = "Can you review my code?", timestamp = System.currentTimeMillis(), chatId = 1, senderId = 4),
            Message(content = "We need to finalize the budget.", timestamp = System.currentTimeMillis(), chatId = 1, senderId = 5)
        )
        taskDao.insertMessages(dummyMessages)
    }


    fun getAllMessages() : LiveData<List<Message>> {
        return taskDao.getAllMessages()
    }


}
