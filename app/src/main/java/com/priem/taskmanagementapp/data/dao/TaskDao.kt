package com.priem.taskmanagementapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.data.entity.Message
import com.priem.taskmanagementapp.data.entity.Task
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

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): LiveData<List<Task>>

    // Labels related queries

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabel(label: Label): Long

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


    // TaskFollowerCrossRef related query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskFollowerCrossRef(crossRef: TaskFollowerCrossRef)


    // Message related query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Message>)

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): LiveData<List<Message>>




}
