package com.priem.taskmanagementapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.model.Attachment

class AttachmentAdapter(private val attachments: List<Attachment>) :
    RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attachment, parent, false)
        return AttachmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        holder.bind(attachments[position])
    }

    override fun getItemCount() = attachments.size

    class AttachmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textAttachmentName: TextView = itemView.findViewById(R.id.textAttachmentName)

        fun bind(attachment: Attachment) {
            textAttachmentName.text = attachment.fileName
        }
    }
}
