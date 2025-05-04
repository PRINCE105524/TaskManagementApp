package com.priem.taskmanagementapp.repository


import androidx.lifecycle.LiveData
import com.priem.taskmanagementapp.data.dao.TaskDao
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.data.entity.Message
import com.priem.taskmanagementapp.data.entity.Task
import com.priem.taskmanagementapp.data.entity.TaskAttachedFile
import com.priem.taskmanagementapp.data.entity.TaskFollowerCrossRef
import com.priem.taskmanagementapp.data.entity.TaskLabelCrossRef
import com.priem.taskmanagementapp.data.entity.User
import com.priem.taskmanagementapp.data.relation.TaskWithLabels

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }


    suspend fun updateTaskMessageId(taskId: Long, messageId: Long) {
        taskDao.updateTaskMessageId(taskId, messageId)
    }

    suspend fun deleteAllLabelsForTask(taskId: Long) = taskDao.deleteAllLabelsForTask(taskId)

    suspend fun deleteAllFollowersForTask(taskId: Long) = taskDao.deleteAllFollowersForTask(taskId)

    suspend fun deleteAllFilesForTask(taskId: Long) = taskDao.deleteAllFilesForTask(taskId)



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

     fun getAllLabels(): LiveData<List<Label>> {
        return taskDao.getAllLabels()
    }

     suspend fun insertLabel(label: Label) {
        taskDao.insertLabel(label)
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

    suspend fun insertUser(user: User) {
        taskDao.insertUser(user)
    }

    // TaskFollowerCrossRef related methods
    suspend fun insertTaskFollowerCrossRef(taskId: Long, userId: Long) {
        taskDao.insertTaskFollowerCrossRef(TaskFollowerCrossRef(taskId, userId))
    }

    suspend fun getUserById(id: Long): User? {
        return taskDao.getUserById(id)
    }


    // Message related methods
// Message related methods
//    suspend fun insertDummyMessages() {
//        val currentTime = System.currentTimeMillis()
//
//        val dummyMessages = listOf(
//            Message(
//                content = "Hey, are you coming to the meeting?",
//                timestamp = currentTime,
//                chatId = 1,
//                senderId = 1,
//                contentType = "TEXT",
//                contentJson = null
//            ),
//            Message(
//                content = "Don't forget to submit the report.",
//                timestamp = currentTime,
//                chatId = 1,
//                senderId = 2,
//                contentType = "TEXT",
//                contentJson = null
//            ),
//            Message(
//                content = "TASK: Design Review",
//                timestamp = currentTime,
//                chatId = 1,
//                senderId = 3,
//                contentType = "TASK",
//                contentJson = com.google.gson.Gson().toJson(
//                    com.priem.taskmanagementapp.data.model.TaskData(
//                        taskId = 1,
//                        title = "Design Review",
//                        description = "Review the new mobile app design before release.",
//                        priority = "HIGH",
//                        dueTimestamp = currentTime + 86400000, // 1 day later
//                        labels = listOf("Review", "UI/UX"),
//                        followers = listOf(
//                            com.priem.taskmanagementapp.data.model.FollowerData(101, "Alice"),
//                            com.priem.taskmanagementapp.data.model.FollowerData(102, "Bob")
//                        ),
//                        attachedMessages = listOf(1, 2),
//                        attachedFiles = listOf()
//                    )
//                )
//            ),
//            Message(
//                content = "TASK: Budget Planning",
//                timestamp = currentTime,
//                chatId = 1,
//                senderId = 4,
//                contentType = "TASK",
//                contentJson = com.google.gson.Gson().toJson(
//                    com.priem.taskmanagementapp.data.model.TaskData(
//                        taskId = 2,
//                        title = "Budget Planning",
//                        description = "Prepare budget plan for next quarter.",
//                        priority = "MEDIUM",
//                        dueTimestamp = currentTime + 172800000, // 2 days later
//                        labels = listOf("Finance", "Planning"),
//                        followers = listOf(
//                            com.priem.taskmanagementapp.data.model.FollowerData(103, "Charlie")
//                        ),
//                        attachedMessages = emptyList(),
//                        attachedFiles = emptyList()
//                    )
//                )
//            ),
//            Message(
//                content = "Here is the design file.",
//                timestamp = currentTime,
//                chatId = 1,
//                senderId = 5,
//                contentType = "TEXT",
//                contentJson = null
//            )
//        )
//
//        taskDao.insertMessages(dummyMessages)
//    }


    suspend fun insertMessage(message: Message): Long {
        return taskDao.insertMessages(message)
    }

    suspend fun updateMessageContentJson(messageId: Long, contentJson: String) {
        taskDao.updateMessageContentJson(messageId, contentJson)
    }

    fun getAllMessages() : LiveData<List<Message>> {
        return taskDao.getAllMessages()
    }

    // File related methods
    suspend fun insertTaskAttachedFile(file: TaskAttachedFile) {
        taskDao.insertTaskAttachedFile(file)
    }



}
