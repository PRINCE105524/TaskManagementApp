package com.priem.taskmanagementapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.database.TaskDatabase
import com.priem.taskmanagementapp.data.model.FileData
import com.priem.taskmanagementapp.data.model.TaskData
import com.priem.taskmanagementapp.ui.adapter.AttachedFilesAdapter
import com.priem.taskmanagementapp.ui.adapter.AttachmentsAdapter
import com.priem.taskmanagementapp.ui.adapter.FollowersAdapter
import com.priem.taskmanagementapp.utility.InsetsHelper
import kotlinx.coroutines.launch

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var textTaskTitle: TextView
    private lateinit var textTaskDescription: TextView
    private lateinit var textReminder: TextView
    private lateinit var chipPriority: Chip
    private lateinit var chipGroupLabels: ChipGroup
    private lateinit var recyclerViewFollowers: RecyclerView
    private lateinit var recyclerViewAttachments: RecyclerView

    private lateinit var followersAdapter: FollowersAdapter
    private lateinit var attachmentsAdapter: AttachmentsAdapter

    private lateinit var taskData: TaskData
    private var messageId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        val rootView = findViewById<View>(R.id.nestedScrollView)
        InsetsHelper.setupWindowInsets(window, rootView, useEdgeToEdge = true)

        initViews()

        findViewById<Button>(R.id.buttonEditTask).setOnClickListener {
            openEditorForEditing()
        }

        loadTaskData()
    }

    private fun openEditorForEditing() {
        val intent = Intent(this, TaskEditorActivity::class.java)
        intent.putExtra("taskDataJson", Gson().toJson(taskData)) // pass full task data
        intent.putExtra("isEditing", true) // tell editor it's edit mode
        startActivity(intent)
    }


    private fun initViews() {
        textTaskTitle = findViewById(R.id.textTaskTitle)
        textTaskDescription = findViewById(R.id.textTaskDescription)
        textReminder = findViewById(R.id.textReminder)
        chipPriority = findViewById(R.id.chipPriority)
        chipGroupLabels = findViewById(R.id.chipGroupLabels)
        recyclerViewFollowers = findViewById(R.id.recyclerViewFollowers)
        recyclerViewAttachments = findViewById(R.id.recyclerViewAttachments)

        recyclerViewFollowers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAttachments.layoutManager = LinearLayoutManager(this)
    }

    private fun loadTaskData() {
        val taskDataJson = intent.getStringExtra("taskDataJson")
        taskData = Gson().fromJson(taskDataJson, TaskData::class.java)
        messageId = taskData.messageId!!
        bindTaskData()
    }

    override fun onResume() {
        super.onResume()
        if (messageId != -1L) {
            lifecycleScope.launch {
                val dao = TaskDatabase.getDatabase(this@TaskDetailsActivity).taskDao()
                val updatedMessage = dao.getMessageById(messageId)
                val updatedTaskData = updatedMessage?.contentJson?.let {
                    Gson().fromJson(it, TaskData::class.java)
                }
                updatedTaskData?.let {
                    taskData = it
                    bindTaskData()
                }
            }
        }
    }



    private fun bindTaskData() {
        textTaskTitle.text = taskData.title
        textTaskDescription.text = taskData.description

        if (taskData.dueTimestamp != null) {
            val dateFormatted = android.text.format.DateFormat.format("MMM dd, yyyy 'at' hh:mm a", taskData.dueTimestamp!!)
            textReminder.text = "Reminder: $dateFormatted"
            textReminder.isVisible = true
        } else {
            textReminder.isVisible = false
        }

        chipPriority.text = taskData.priority
        chipPriority.chipBackgroundColor = when (taskData.priority) {
            "HIGH" -> getColorStateList(R.color.purple_200)
            "LOW" -> getColorStateList(R.color.teal_700)
            else -> getColorStateList(R.color.teal_200)
        }

        // Labels
        chipGroupLabels.removeAllViews()
        taskData.labels?.forEach { label ->
            val chip = Chip(this).apply {
                text = label
                isClickable = false
                isCheckable = false
            }
            chipGroupLabels.addView(chip)
        }

        // Followers
        followersAdapter = FollowersAdapter(taskData.followers!!)
        recyclerViewFollowers.adapter = followersAdapter

        // Attachments (messages)
        if (!taskData.attachedMessages.isNullOrEmpty()) {
            loadAttachedMessages(taskData.attachedMessages!!)
        }

        // Attachments (files)
        if (!taskData.attachedFiles.isNullOrEmpty()) {
            loadAttachedFiles(taskData.attachedFiles!!)
        }

    }

    private fun loadAttachedMessages(messageIds: List<Long>) {
        lifecycleScope.launch {
            val dao = TaskDatabase.getDatabase(this@TaskDetailsActivity).taskDao()
            val messages = dao.getMessagesByIds(messageIds)
            attachmentsAdapter = AttachmentsAdapter(messages)
            recyclerViewAttachments.adapter = attachmentsAdapter
        }
    }

    private fun loadAttachedFiles(fileDataList: List<FileData>) {
        val attachedFilesAdapter = AttachedFilesAdapter(fileDataList)
        findViewById<RecyclerView>(R.id.recyclerViewAttachedFiles).adapter = attachedFilesAdapter
    }

}


