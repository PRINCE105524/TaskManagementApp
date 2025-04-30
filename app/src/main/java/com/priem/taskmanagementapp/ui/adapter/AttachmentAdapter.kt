package com.priem.taskmanagementapp.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.model.Attachment
import java.io.File

class AttachmentAdapter(
    private val attachments: MutableList<Attachment>,
    private val onDeleteClicked: (Attachment) -> Unit
) : RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attachment, parent, false)
        return AttachmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        holder.bind(attachments[position])
    }

    override fun getItemCount() = attachments.size

    inner class AttachmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textAttachmentName: TextView = itemView.findViewById(R.id.textAttachmentName)
        private val imageAttachmentIcon: ImageView = itemView.findViewById(R.id.imageAttachmentIcon)
        private val btnRemove: ImageButton = itemView.findViewById(R.id.btnRemoveAttachment)

        fun bind(attachment: Attachment) {
            textAttachmentName.text = attachment.fileName

            val fileType = attachment.fileType?.lowercase() ?: "unknown"
            val file = File(attachment.filePath)

            when {
                fileType.startsWith("image/") && file.exists() -> {
                    imageAttachmentIcon.setImageURI(Uri.fromFile(file))
                }
                fileType == "application/pdf" -> {
                    imageAttachmentIcon.setImageResource(R.drawable.ic_pdf)
                }
                fileType.contains("word") -> {
                    imageAttachmentIcon.setImageResource(R.drawable.ic_doc)
                }
                fileType.contains("zip") || fileType.contains("rar") -> {
                    imageAttachmentIcon.setImageResource(R.drawable.ic_zip)
                }
                else -> {
                    imageAttachmentIcon.setImageResource(R.drawable.ic_file)
                }
            }

            btnRemove.setOnClickListener {
                onDeleteClicked(attachment)
            }
        }
    }
}


