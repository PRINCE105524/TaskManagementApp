package com.priem.taskmanagementapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.entity.Message
import com.priem.taskmanagementapp.data.model.TaskData
import com.google.gson.Gson

class MessageAdapter(
    private val messages: List<Message>,
    private val onTaskClicked: (TaskData) -> Unit,
    private val onMessageClicked: (Message) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_TEXT = 1
        private const val VIEW_TYPE_TASK = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].contentType == "TASK") VIEW_TYPE_TASK else VIEW_TYPE_TEXT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_TASK) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_task, parent, false)
            TaskViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_normal, parent, false)
            NormalViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is TaskViewHolder) {
            holder.bind(message, onTaskClicked)
        } else if (holder is NormalViewHolder) {
            holder.bind(message, onMessageClicked)
        }
    }

    override fun getItemCount() = messages.size

    class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessageContent: TextView = itemView.findViewById(R.id.textMessageContent)

        fun bind(message: Message, onMessageClicked: (Message) -> Unit) {
            textMessageContent.text = message.content
            itemView.setOnClickListener {
                onMessageClicked(message)
            }
        }

    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTaskTitle: TextView = itemView.findViewById(R.id.textTaskTitle)
        private val textTaskPriority: TextView = itemView.findViewById(R.id.textTaskPriority)
        private val textTaskDue: TextView = itemView.findViewById(R.id.textTaskDue)

        fun bind(message: Message, onTaskClicked: (TaskData) -> Unit) {
            val taskData = Gson().fromJson(message.contentJson, TaskData::class.java)

            textTaskTitle.text = taskData.title
            textTaskPriority.text = "Priority: ${taskData.priority}"

            taskData.dueTimestamp?.let { timestamp ->
                val dueText = android.text.format.DateFormat.format("MMM dd, yyyy hh:mm a", timestamp)
                textTaskDue.text = "Due: $dueText"
            }

            itemView.setOnClickListener {
                onTaskClicked(taskData)
            }
        }
    }
}
