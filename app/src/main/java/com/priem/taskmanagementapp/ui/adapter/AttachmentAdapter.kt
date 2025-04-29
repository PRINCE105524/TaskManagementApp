package com.priem.taskmanagementapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.model.Attachment
import java.io.File

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
        private val imageAttachmentIcon: ImageView = itemView.findViewById(R.id.imageAttachmentIcon)

        fun bind(attachment: Attachment) {
            textAttachmentName.text = attachment.fileName

            val fileType = attachment.fileType?.lowercase() ?: "unknown"

            when {
                fileType.startsWith("image/") -> {
                    // Try load image preview
                    val file = File(attachment.filePath)
                    if (file.exists()) {
                        imageAttachmentIcon.setImageURI(android.net.Uri.fromFile(file))
                    } else {
                        imageAttachmentIcon.setImageResource(R.drawable.ic_image_placeholder) // fallback
                    }
                }
                fileType == "application/pdf" -> {
                    imageAttachmentIcon.setImageResource(R.drawable.ic_pdf)
                }
                fileType == "application/msword" || fileType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> {
                    imageAttachmentIcon.setImageResource(R.drawable.ic_doc)
                }
                fileType == "application/zip" || fileType == "application/x-rar-compressed" -> {
                    imageAttachmentIcon.setImageResource(R.drawable.ic_zip)
                }
                else -> {
                    imageAttachmentIcon.setImageResource(R.drawable.ic_file) // default file icon
                }
            }
        }
    }
}

