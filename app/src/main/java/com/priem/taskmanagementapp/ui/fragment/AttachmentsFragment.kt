package com.priem.taskmanagementapp.ui.fragment

import android.app.Activity
import android.content.Intent
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

class AttachmentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonPickFile: Button
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var adapter: AttachmentAdapter

    private val attachments = mutableListOf<Attachment>()

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { addAttachment(it) }
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
        filePickerLauncher.launch("*/*") // Pick any file
    }

    private fun addAttachment(uri: Uri) {
        val fileName = File(uri.path ?: "").name
        val fileType = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(requireContext().contentResolver.getType(uri)) ?: "unknown"
        val fileSize = uri.let {
            requireContext().contentResolver.openInputStream(it)?.available()?.toLong() ?: 0L
        }

        val attachment = Attachment(
            fileName = fileName,
            filePath = uri.toString(),
            fileSize = fileSize,
            fileType = fileType
        )

        attachments.add(attachment)
        adapter.notifyItemInserted(attachments.size - 1)

        taskViewModel.setAttachments(attachments)
    }
}

