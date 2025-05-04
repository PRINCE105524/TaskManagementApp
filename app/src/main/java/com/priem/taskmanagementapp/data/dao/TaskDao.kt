package com.priem.taskmanagementapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.data.entity.Message
import com.priem.taskmanagementapp.data.entity.Task
import com.priem.taskmanagementapp.data.entity.TaskAttachedFile
import com.priem.taskmanagementapp.data.entity.TaskFollowerCrossRef
import com.priem.taskmanagementapp.data.entity.TaskLabelCrossRef
import com.priem.taskmanagementapp.data.entity.User
import com.priem.taskmanagementapp.data.relation.TaskWithLabels

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)


    @Query("UPDATE tasks SET messageId = :messageId WHERE taskId = :taskId")
    suspend fun updateTaskMessageId(taskId: Long, messageId: Long)


    @Query("DELETE FROM task_labels WHERE taskId = :taskId")
    suspend fun deleteAllLabelsForTask(taskId: Long)

    @Query("DELETE FROM TaskFollowerCrossRef WHERE taskId = :taskId")
    suspend fun deleteAllFollowersForTask(taskId: Long)

    @Query("DELETE FROM task_attached_files WHERE taskId = :taskId")
    suspend fun deleteAllFilesForTask(taskId: Long)


    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): LiveData<List<Task>>

    // Labels related queries

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabel(label: Label): Long


    @Query("SELECT * FROM labels")
    fun getAllLabels(): LiveData<List<Label>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskLabelCrossRef(crossRef: TaskLabelCrossRef)

    @Transaction
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasksWithLabels(): LiveData<List<TaskWithLabels>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabelAndReturnId(label: Label): Long

    @Query("SELECT labelId FROM labels WHERE name = :name LIMIT 1")
    suspend fun getLabelIdByName(name: String): Long?



    // User related query

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<User>)

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun getUserById(id: Long): User?


    // TaskFollowerCrossRef related query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskFollowerCrossRef(crossRef: TaskFollowerCrossRef)


    // Message related query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: Message): Long

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): LiveData<List<Message>>

    @Query("UPDATE messages SET contentJson = :contentJson WHERE messageId = :messageId")
    suspend fun updateMessageContentJson(messageId: Long, contentJson: String)


    @Query("SELECT * FROM messages WHERE messageId = :id")
    suspend fun getMessageById(id: Long): Message?

    // File related query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskAttachedFile(file: TaskAttachedFile)

    @Query("SELECT * FROM messages WHERE messageId IN (:ids)")
    suspend fun getMessagesByIds(ids: List<Long>): List<Message>




}
