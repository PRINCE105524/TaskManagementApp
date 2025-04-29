package com.priem.taskmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.priem.taskmanagementapp.data.database.TaskDatabase
import com.priem.taskmanagementapp.data.entity.Message
import com.priem.taskmanagementapp.repository.TaskRepository
import com.priem.taskmanagementapp.ui.TaskDetailsActivity
import com.priem.taskmanagementapp.ui.TaskEditorActivity
import com.priem.taskmanagementapp.ui.adapter.MessageAdapter
import com.priem.taskmanagementapp.utility.InsetsHelper
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddMessage: FloatingActionButton
    private lateinit var adapter: MessageAdapter
    private lateinit var repository: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootView = findViewById<View>(R.id.rootLayout)

        // true if you want immersive experience, false if you want normal behavior
        InsetsHelper.setupWindowInsets(window, rootView, useEdgeToEdge = true)

        val dao = TaskDatabase.getDatabase(this).taskDao()
        repository = TaskRepository(dao)

        recyclerView = findViewById(R.id.recyclerViewMessages)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddMessage = findViewById(R.id.fabAddMessage)

        fabAddMessage.setOnClickListener {
            showAddMessageDialog()
        }

        loadMessages()
    }

    private fun loadMessages() {
        lifecycleScope.launch {
            repository.getAllMessages().observe(this@MainActivity) { messages ->

                adapter = MessageAdapter(
                    messages,
                    onTaskClicked = { taskData ->
                        val intent = Intent(this@MainActivity, TaskDetailsActivity::class.java)
                        intent.putExtra("taskDataJson", Gson().toJson(taskData))
                        startActivity(intent)
                    },
                    onMessageClicked = { message ->
                        val intent = Intent(this@MainActivity, TaskEditorActivity::class.java)
                        intent.putExtra("attachedMessageId", message.messageId)
                        startActivity(intent)
                    }
                )

                recyclerView.adapter = adapter
            }
        }
    }

    private fun showAddMessageDialog() {
        val input = EditText(this)
        input.hint = "Enter your single line note. Later you can convert this to Task!"

        AlertDialog.Builder(this)
            .setTitle("New Note")
            .setView(input)
            .setPositiveButton("Send") { dialog, _ ->
                val messageText = input.text.toString().trim()
                if (messageText.isNotEmpty()) {
                    insertNewMessage(messageText)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun insertNewMessage(content: String) {
        lifecycleScope.launch {
            val message = Message(
                content = content,
                timestamp = System.currentTimeMillis(),
                chatId = 1,
                senderId = 1,
                contentType = "TEXT",
                contentJson = null
            )
            repository.insertMessage(message)
        }
    }
}