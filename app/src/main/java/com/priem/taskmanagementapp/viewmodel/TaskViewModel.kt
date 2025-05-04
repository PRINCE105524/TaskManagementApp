package com.priem.taskmanagementapp.viewmodel

import androidx.lifecycle.*
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.data.entity.Message
import com.priem.taskmanagementapp.data.entity.Task
import com.priem.taskmanagementapp.data.entity.TaskAttachedFile
import com.priem.taskmanagementapp.data.entity.TaskLabelCrossRef
import com.priem.taskmanagementapp.data.entity.User
import com.priem.taskmanagementapp.data.model.Attachment
import com.priem.taskmanagementapp.data.relation.TaskWithLabels
import com.priem.taskmanagementapp.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Details Fragment
    private val _taskTitle = androidx.lifecycle.MutableLiveData<String>()
    val taskTitle: androidx.lifecycle.LiveData<String> = _taskTitle

    private val _taskDescription = androidx.lifecycle.MutableLiveData<String>()
    val taskDescription: androidx.lifecycle.LiveData<String> = _taskDescription

    fun setTitle(title: String) {
        _taskTitle.value = title
    }

    fun setDescription(description: String) {
        _taskDescription.value = description
    }


    // Label Fragment
    private val _taskLabels = androidx.lifecycle.MutableLiveData<List<String>>(emptyList())
    val taskLabels: androidx.lifecycle.LiveData<List<String>> = _taskLabels

    fun setLabels(labels: List<String>) {
        _taskLabels.value = labels
    }


    // Labels LiveData
    private val _availableLabels = MutableLiveData<List<Label>>(emptyList())
    val availableLabels: LiveData<List<Label>> get() = _availableLabels

    // Load labels from DB
    fun loadLabels() {
        viewModelScope.launch {
            repository.getAllLabels().observeForever { labels ->
                _availableLabels.value = labels
            }
        }
    }

    // Insert a new label
    suspend fun insertLabel(name: String) {
            repository.insertLabel(Label(name = name))
    }


    // Calendar Fragment
    private val _taskDueTimestamp = androidx.lifecycle.MutableLiveData<Long?>()
    val taskDueTimestamp: androidx.lifecycle.LiveData<Long?> = _taskDueTimestamp

    fun setDueTimestamp(timestamp: Long) {
        _taskDueTimestamp.value = timestamp
    }

    // Time Fragment
    private val _taskDueTime = androidx.lifecycle.MutableLiveData<String?>()
    val taskDueTime: androidx.lifecycle.LiveData<String?> = _taskDueTime

    fun setDueTime(time: String) {
        _taskDueTime.value = time
    }

    // Followers Fragment
    private val _taskFollowers = androidx.lifecycle.MutableLiveData<List<Long>>()
    val taskFollowers: androidx.lifecycle.LiveData<List<Long>> = _taskFollowers

    fun setFollowers(followerIds: List<Long>) {
        _taskFollowers.value = followerIds
    }

    suspend fun insertTaskFollowerCrossRef(taskId: Long, userId: Long) {
        repository.insertTaskFollowerCrossRef(taskId, userId)
    }

    suspend fun getUserById(id: Long): User? {
        return repository.getUserById(id)
    }

    // Priority Fragment
    private val _taskPriority = androidx.lifecycle.MutableLiveData<String>("LOW")
    val taskPriority: androidx.lifecycle.LiveData<String> = _taskPriority

    fun setTaskPriority(priority: String) {
        _taskPriority.value = priority
    }


    // Attachments Fragment
    private val _taskAttachments = androidx.lifecycle.MutableLiveData<List<Attachment>>()
    val taskAttachments: androidx.lifecycle.LiveData<List<Attachment>> = _taskAttachments

    fun setAttachments(attachmentList: List<Attachment>) {
        _taskAttachments.value = attachmentList
    }

    suspend fun insertTaskAttachedFile(file: TaskAttachedFile) {
        repository.insertTaskAttachedFile(file)
    }



    val allTasks: LiveData<List<Task>> = repository.allTasks

    fun insert(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }


    suspend fun insertAndReturnTask(task: Task): Long {
        return repository.insertTask(task)
    }

    fun updateTaskMessageId(taskId: Long, messageId: Long) {
        viewModelScope.launch {
            repository.updateTaskMessageId(taskId, messageId)
        }
    }

    suspend fun insertLabelAndReturnId(label: Label): Long {
        return repository.insertLabelAndReturnId(label)
    }

    suspend fun getLabelIdByName(labelName: String): Long? {
        return repository.getLabelIdByName(labelName)
    }

    fun insertTaskLabelCrossRef(taskId: Long, labelId: Long) = viewModelScope.launch {
        repository.insertTaskLabelCrossRef(TaskLabelCrossRef(taskId, labelId))
    }

    // insert task message
    suspend fun insertMessage(message: Message): Long {
        return repository.insertMessage(message)
    }

    fun updateMessageContentJson(messageId: Long, contentJson: String) {
        viewModelScope.launch {
            repository.updateMessageContentJson(messageId, contentJson)
        }
    }



    // Task Related

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteAllLabelsForTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteAllLabelsForTask(taskId)
        }
    }

    fun deleteAllFollowersForTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteAllFollowersForTask(taskId)
        }
    }

    fun deleteAllFilesForTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteAllFilesForTask(taskId)
        }
    }


}
