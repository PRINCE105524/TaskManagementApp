package com.priem.taskmanagementapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.repository.TaskRepository
import com.priem.taskmanagementapp.viewmodel.TaskViewModel
import com.priem.taskmanagementapp.viewmodel.TaskViewModelFactory
import com.priem.taskmanagementapp.data.database.TaskDatabase
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.data.entity.Message
import com.priem.taskmanagementapp.data.entity.Task
import com.priem.taskmanagementapp.data.entity.TaskAttachedFile
import com.priem.taskmanagementapp.data.entity.User
import com.priem.taskmanagementapp.data.model.Attachment
import com.priem.taskmanagementapp.data.model.TaskData
import com.priem.taskmanagementapp.ui.adapter.TaskEditorAdapter
import com.priem.taskmanagementapp.utility.InsetsHelper
import kotlinx.coroutines.launch

class TaskEditorActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    lateinit var taskViewModel: TaskViewModel
    private var attachedMessageId: Long? = null
    private var editingTaskData: TaskData? = null

    private val tabTitles = listOf(
        "Details", "Labels", "Calendar", "Time", "Followers", "Priority", "Attachments"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_editor)

        val rootView = findViewById<View>(R.id.editorConstraintLayout)
        InsetsHelper.setupWindowInsets(window, rootView, useEdgeToEdge = true)

        // ✅ Step 1 — Move this BEFORE setupViewModel()
        attachedMessageId = intent.getLongExtra("attachedMessageId", -1).takeIf { it != -1L }

        intent.getStringExtra("taskDataJson")?.let { json ->
            editingTaskData = Gson().fromJson(json, TaskData::class.java)
        }

        // ✅ Now taskViewModel will pick up editingTaskData
        setupViewModel()
        setupViewPager()
        setupTabLayout()

        val buttonSave: Button = findViewById(R.id.buttonSave)
        buttonSave.text = if (editingTaskData == null) "SAVE TASK" else "UPDATE TASK"
        buttonSave.setOnClickListener {
            saveTask()
        }
    }



    private fun saveTask() {
        val title = taskViewModel.taskTitle.value.orEmpty().trim()
        val description = taskViewModel.taskDescription.value.orEmpty().trim()
        val selectedLabels = taskViewModel.taskLabels.value.orEmpty()
        val taskPriority = taskViewModel.taskPriority.value ?: "LOW"

        val dateMillis = taskViewModel.taskDueTimestamp.value
        val timeText = taskViewModel.taskDueTime.value

        var finalDueTimestamp: Long? = null
        if (dateMillis != null) {
            if (timeText != null) {
                val calendar = java.util.Calendar.getInstance()
                calendar.timeInMillis = dateMillis
                val hour = timeText.split(":")[0].toInt()
                val minute = timeText.split(":")[1].toInt()
                calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
                calendar.set(java.util.Calendar.MINUTE, minute)
                calendar.set(java.util.Calendar.SECOND, 0)
                calendar.set(java.util.Calendar.MILLISECOND, 0)
                finalDueTimestamp = calendar.timeInMillis
            } else {
                finalDueTimestamp = dateMillis
            }
        }

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            var taskId: Long
            if (editingTaskData != null) {
                taskId = editingTaskData!!.taskId
                val updatedTask = Task(
                    taskId = taskId,
                    title = title,
                    description = description,
                    priority = taskPriority,
                    dueTimestamp = finalDueTimestamp
                )
                taskViewModel.updateTask(updatedTask)
                taskViewModel.deleteAllLabelsForTask(taskId)
                taskViewModel.deleteAllFollowersForTask(taskId)
                taskViewModel.deleteAllFilesForTask(taskId)
            } else {
                val newTask = Task(
                    title = title,
                    description = description,
                    priority = taskPriority,
                    dueTimestamp = finalDueTimestamp
                )
                taskId = taskViewModel.insertAndReturnTask(newTask)
            }

            selectedLabels.forEach { labelName ->
                val labelId = taskViewModel.getLabelIdByName(labelName)
                    ?: taskViewModel.insertLabelAndReturnId(Label(name = labelName))
                taskViewModel.insertTaskLabelCrossRef(taskId, labelId)
            }

            val selectedFollowers = taskViewModel.taskFollowers.value.orEmpty()
            selectedFollowers.forEach { userId ->
                taskViewModel.insertTaskFollowerCrossRef(taskId, userId)
            }
            val followersList = selectedFollowers.mapNotNull { taskViewModel.getUserById(it) }

            val attachments = taskViewModel.taskAttachments.value.orEmpty()
            attachments.forEach { attachment ->
                val file = TaskAttachedFile(
                    taskId = taskId,
                    fileName = attachment.fileName,
                    filePath = attachment.filePath,
                    fileSize = attachment.fileSize,
                    fileType = attachment.fileType
                )
                taskViewModel.insertTaskAttachedFile(file)
            }

            val messageId = saveTaskAsMessage(taskId, title, description, taskPriority, finalDueTimestamp, selectedLabels, followersList, attachments)
            // Need to update the messageId of Task Table
            taskViewModel.updateTaskMessageId(taskId, messageId)

            Toast.makeText(this@TaskEditorActivity, "Task Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    private suspend fun saveTaskAsMessage(
        taskId: Long,
        title: String,
        description: String,
        taskPriority: String,
        finalDueTimestamp: Long?,
        selectedLabels: List<String>,
        selectedFollowers: List<User>,
        attachments: List<Attachment>
    ): Long {
        val messageId = if (editingTaskData != null) {
            editingTaskData!!.messageId  // 🧠 Reuse the original message ID
        } else {
            val newMessage = Message(
                content = "TASK: $title",
                timestamp = System.currentTimeMillis(),
                chatId = 1,
                senderId = 1,
                contentType = "TASK",
                contentJson = null
            )
            taskViewModel.insertMessage(newMessage)
        }

        val finalTaskJson = buildTaskJson(
            taskId = taskId,
            messageId = messageId!!,
            title = title,
            description = description,
            priority = taskPriority,
            dueTimestamp = finalDueTimestamp,
            labels = selectedLabels,
            followers = selectedFollowers,
            attachedMessages = buildList {
                attachedMessageId?.let { add(it) }
            },
            attachedFiles = attachments
        )

        // 🔁 Update existing message contentJson
        taskViewModel.updateMessageContentJson(messageId, finalTaskJson)

        return messageId
    }

    private fun buildTaskJson(
        taskId: Long,
        messageId: Long,
        title: String,
        description: String,
        priority: String,
        dueTimestamp: Long?,
        labels: List<String>,
        followers: List<User>,
        attachedMessages: List<Long>,
        attachedFiles: List<Attachment>?
    ): String {
        val taskData = com.priem.taskmanagementapp.data.model.TaskData(
            taskId = taskId,
            messageId = messageId,
            title = title,
            description = description,
            priority = priority,
            dueTimestamp = dueTimestamp,
            labels = labels,
            followers = followers.map { com.priem.taskmanagementapp.data.model.FollowerData(it.userId, it.name, it.avatarUrl) },
            attachedMessages = attachedMessages,
            attachedFiles = attachedFiles?.mapIndexed { index, file ->
                com.priem.taskmanagementapp.data.model.FileData(
                    fileId = index.toLong(), // Dummy ID for now
                    fileName = file.fileName,
                    fileUrl = file.filePath,
                    fileSize = file.fileSize,
                    fileType = file.fileType
                )
            }
        )

        return com.google.gson.Gson().toJson(taskData)
    }


    private fun setupViewModel() {
        val dao = TaskDatabase.getDatabase(this).taskDao()
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)
        taskViewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        editingTaskData?.let { task ->
            taskViewModel.setTitle(task.title)
            taskViewModel.setDescription(task.description)
            taskViewModel.setLabels(task.labels ?: emptyList())
            task.dueTimestamp?.let { timestamp ->
                taskViewModel.setDueTimestamp(timestamp)
                // 🧠 Extract time from timestamp and set as HH:mm
                val calendar = java.util.Calendar.getInstance().apply {
                    timeInMillis = timestamp
                }
                val hour = calendar.get(java.util.Calendar.HOUR).let { if (it == 0) 12 else it } // 12-hour clock
                val minute = calendar.get(java.util.Calendar.MINUTE)
                val amPm = if (calendar.get(java.util.Calendar.AM_PM) == java.util.Calendar.AM) "AM" else "PM"
                val formattedTime = String.format("%02d:%02d %s", hour, minute, amPm)
                taskViewModel.setDueTime(formattedTime)
            }
            taskViewModel.setTaskPriority(task.priority)
            taskViewModel.setFollowers(task.followers?.map { it.userId } ?: emptyList())
            taskViewModel.setAttachments(task.attachedFiles?.map {
                Attachment(
                    fileName = it.fileName,
                    filePath = it.fileUrl ?: "",
                    fileSize = it.fileSize ?: 0L,
                    fileType = it.fileType ?: "application/octet-stream"
                )
            } ?: emptyList())
        }
    }

    private fun setupViewPager() {
        viewPager = findViewById(R.id.viewPagerTaskEditor)
        val adapter = TaskEditorAdapter(this)
        viewPager.adapter = adapter
    }

    private fun setupTabLayout() {
        tabLayout = findViewById(R.id.tabLayoutTaskEditor)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}


