package com.priem.taskmanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.priem.taskmanagementapp.ui.theme.TaskManagementAppTheme
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.priem.taskmanagementapp.data.database.TaskDatabase
import com.priem.taskmanagementapp.repository.TaskRepository
import com.priem.taskmanagementapp.ui.TaskEditorActivity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagementAppTheme {
                MainScreen()
            }
        }
        lifecycleScope.launch {
            val dao = TaskDatabase.getDatabase(this@MainActivity).taskDao()
            val repository = TaskRepository(dao)
            if (!repository.getAllUsers().value.isNullOrEmpty() && !repository.getAllMessages().value.isNullOrEmpty()){
                repository.insertDummyUsers()
                repository.insertDummyMessages()
            }
        }
    }

    @Composable
    fun MainScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Task Manager!",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val intent = Intent(this@MainActivity, TaskEditorActivity::class.java)
                    startActivity(intent)
                }
            ) {
                Text(text = "Create New Task")
            }

        }
    }
}
