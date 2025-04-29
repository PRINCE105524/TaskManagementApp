package com.priem.taskmanagementapp.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.model.Attachment
import com.priem.taskmanagementapp.ui.adapter.AttachmentAdapter
import com.priem.taskmanagementapp.viewmodel.TaskViewModel
import java.io.File
import java.io.FileOutputStream

class AttachmentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonPickFile: Button
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var adapter: AttachmentAdapter

    private val attachments = mutableListOf<Attachment>()

    private val multipleFilePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris: List<Uri>? ->
        uris?.forEach { uri ->
            copyFileToInternalStorage(uri)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_attachments, container, false)

        buttonPickFile = view.findViewById(R.id.buttonPickFile)
        recyclerView = view.findViewById(R.id.recyclerViewAttachments)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = AttachmentAdapter(attachments)
        recyclerView.adapter = adapter

        buttonPickFile.setOnClickListener {
            pickFile()
        }

        return view
    }

    private fun pickFile() {
        multipleFilePickerLauncher.launch(arrayOf("*/*"))
    }

    private fun copyFileToInternalStorage(uri: Uri) {
        try {
            val contentResolver = requireContext().contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val fileExtension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(uri)) ?: "file"

            val fileName = "attachment_${System.currentTimeMillis()}.$fileExtension"
            val file = File(requireContext().filesDir, fileName)
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            val fileSize = file.length()

            val attachment = Attachment(
                fileName = file.name,
                filePath = file.absolutePath,
                fileSize = fileSize,
                fileType = contentResolver.getType(uri) ?: "unknown"
            )

            attachments.add(attachment)
            adapter.notifyItemInserted(attachments.size - 1)

            taskViewModel.setAttachments(attachments)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

