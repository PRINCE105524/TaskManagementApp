package com.priem.taskmanagementapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.repository.TaskRepository
import com.priem.taskmanagementapp.viewmodel.TaskViewModel
import com.priem.taskmanagementapp.viewmodel.TaskViewModelFactory
import com.priem.taskmanagementapp.data.database.TaskDatabase
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.ui.adapter.TaskEditorAdapter
import kotlinx.coroutines.launch

class TaskEditorActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    lateinit var taskViewModel: TaskViewModel

    private val tabTitles = listOf(
        "Details", "Labels", "Calendar", "Time", "Followers", "Priority", "Attachments"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_editor)

        val buttonSave: Button = findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            saveTask()
        }


        setupViewModel()
        setupViewPager()
        setupTabLayout()
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
                // 🧠 Both Date and Time selected
                val calendar = java.util.Calendar.getInstance()
                calendar.timeInMillis = dateMillis // Set date

                val hour = timeText.split(":")[0].toInt()
                val minute = timeText.split(":")[1].toInt()

                calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
                calendar.set(java.util.Calendar.MINUTE, minute)
                calendar.set(java.util.Calendar.SECOND, 0)
                calendar.set(java.util.Calendar.MILLISECOND, 0)

                finalDueTimestamp = calendar.timeInMillis
            } else {
                // Only Date selected, Time is null
                finalDueTimestamp = dateMillis
            }
        }
        // If no date selected → leave finalDueTimestamp as null

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {

            val task = com.priem.taskmanagementapp.data.entity.Task(
                title = title,
                description = description,
                priority = taskPriority,
                dueTimestamp = finalDueTimestamp
            )

            val taskId = taskViewModel.insertAndReturnTask(task)

            selectedLabels.forEach { labelName ->
                val existingLabelId = taskViewModel.getLabelIdByName(labelName)

                val labelId = if (existingLabelId != null) {
                    existingLabelId
                } else {
                    taskViewModel.insertLabelAndReturnId(com.priem.taskmanagementapp.data.entity.Label(name = labelName))
                }

                taskViewModel.insertTaskLabelCrossRef(taskId, labelId)
            }


            val selectedFollowers = taskViewModel.taskFollowers.value.orEmpty()
            selectedFollowers.forEach { userId ->
                taskViewModel.insertTaskFollowerCrossRef(taskId, userId)
            }

            Toast.makeText(this@TaskEditorActivity, "Task Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupViewModel() {
        val dao = TaskDatabase.getDatabase(this).taskDao()
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)
        taskViewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]
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


