package com.priem.taskmanagementapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.viewmodel.TaskViewModel

class PriorityFragment : Fragment() {

    private lateinit var radioGroupPriority: RadioGroup
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_priority, container, false)
        radioGroupPriority = view.findViewById(R.id.radioGroupPriority)

        radioGroupPriority.setOnCheckedChangeListener { _, checkedId ->
            val priority = when (checkedId) {
                R.id.radioLow -> "LOW"
                R.id.radioMedium -> "MEDIUM"
                R.id.radioHigh -> "HIGH"
                else -> "LOW" // Default fallback
            }
            taskViewModel.setTaskPriority(priority)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🧠 Pre-select existing priority if in edit mode
        when (taskViewModel.taskPriority.value.orEmpty()) {
            "LOW" -> radioGroupPriority.check(R.id.radioLow)
            "MEDIUM" -> radioGroupPriority.check(R.id.radioMedium)
            "HIGH" -> radioGroupPriority.check(R.id.radioHigh)
        }
    }
}

