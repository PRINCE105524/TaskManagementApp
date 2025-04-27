package com.priem.taskmanagementapp.viewmodel

import androidx.lifecycle.*
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.data.entity.Task
import com.priem.taskmanagementapp.data.entity.TaskLabelCrossRef
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


    suspend fun insertLabelAndReturnId(label: Label): Long {
        return repository.insertLabelAndReturnId(label)
    }

    suspend fun getLabelIdByName(labelName: String): Long? {
        return repository.getLabelIdByName(labelName)
    }

    fun insertTaskLabelCrossRef(taskId: Long, labelId: Long) = viewModelScope.launch {
        repository.insertTaskLabelCrossRef(TaskLabelCrossRef(taskId, labelId))
    }



}
