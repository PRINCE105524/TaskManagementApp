package com.priem.taskmanagementapp.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.model.FileData
import java.io.File

class AttachedFilesAdapter(private val files: List<FileData>) :
    RecyclerView.Adapter<AttachedFilesAdapter.FileViewHolder>() {

    inner class FileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textFileName: TextView = view.findViewById(R.id.textFileName)
        val imagePreview: ImageView = view.findViewById(R.id.imagePreview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attached_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]

        holder.textFileName.text = file.fileName

        val context = holder.itemView.context

        val fileObject = file.fileUrl?.let { File(it) }

        if (file.fileType?.startsWith("image/") == true && fileObject?.exists() == true) {
            holder.imagePreview.setImageURI(Uri.fromFile(fileObject))
        } else {
            when (file.fileType?.lowercase()) {
                "application/pdf" -> holder.imagePreview.setImageResource(R.drawable.ic_pdf)
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> holder.imagePreview.setImageResource(R.drawable.ic_doc)
                "application/zip",
                "application/x-rar-compressed" -> holder.imagePreview.setImageResource(R.drawable.ic_zip)
                else -> holder.imagePreview.setImageResource(R.drawable.ic_file)
            }
        }

        holder.imagePreview.visibility = View.VISIBLE

        holder.itemView.setOnClickListener {
            if (fileObject != null && fileObject.exists()) {
                openFile(context, fileObject, file.fileType)
            }
        }
    }

    override fun getItemCount(): Int = files.size

    private fun openFile(context: android.content.Context, file: File, mimeType: String?) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, mimeType ?: "*/*")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            context.startActivity(intent)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


