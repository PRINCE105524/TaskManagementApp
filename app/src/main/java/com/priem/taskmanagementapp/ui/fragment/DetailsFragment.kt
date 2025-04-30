package com.priem.taskmanagementapp.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.viewmodel.TaskViewModel

class DetailsFragment : Fragment() {

    private lateinit var editTextTaskTitle: EditText
    private lateinit var editTextTaskDescription: EditText

    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        editTextTaskTitle = view.findViewById(R.id.editTextTaskTitle)
        editTextTaskDescription = view.findViewById(R.id.editTextTaskDescription)

        // 🧠 Prefill values if available
        editTextTaskTitle.setText(taskViewModel.taskTitle.value.orEmpty())
        editTextTaskDescription.setText(taskViewModel.taskDescription.value.orEmpty())

        setupTextWatchers()

        return view
    }


    private fun setupTextWatchers() {
        editTextTaskTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (editTextTaskTitle.hasFocus()) {
                    taskViewModel.setTitle(s.toString())
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editTextTaskDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (editTextTaskDescription.hasFocus()) {
                    taskViewModel.setDescription(s.toString())
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

}
