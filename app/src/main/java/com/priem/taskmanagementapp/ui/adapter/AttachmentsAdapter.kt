package com.priem.taskmanagementapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.entity.Message

class AttachmentsAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<AttachmentsAdapter.AttachmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attachments_message, parent, false)
        return AttachmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    class AttachmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageAvatar: ImageView = itemView.findViewById(R.id.imageAvatar)
        private val textMessage: TextView = itemView.findViewById(R.id.textMessage)

        fun bind(message: Message) {
            textMessage.text = message.content
            imageAvatar.setImageResource(R.drawable.ic_avatar_placeholder)
        }
    }
}
