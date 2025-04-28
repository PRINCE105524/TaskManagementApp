package com.priem.taskmanagementapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.viewmodel.TaskViewModel

class CalendarFragment : Fragment() {

    private lateinit var buttonPickDate: Button
    private lateinit var textSelectedDate: TextView
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        buttonPickDate = view.findViewById(R.id.buttonPickDate)
        textSelectedDate = view.findViewById(R.id.textSelectedDate)

        buttonPickDate.setOnClickListener {
            openDatePicker()
        }

        // 🧠 Pre-fill if already selected
        taskViewModel.taskDueTimestamp.value?.let { timestamp ->
            updateSelectedDate(timestamp)
        }

        return view
    }

    private fun openDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Due Date")
            .build()

        datePicker.show(parentFragmentManager, "datePicker")

        datePicker.addOnPositiveButtonClickListener { selection ->
            // Save timestamp to ViewModel
            taskViewModel.setDueTimestamp(selection)
            updateSelectedDate(selection)
        }
    }

    private fun updateSelectedDate(timestamp: Long) {
        val formattedDate = android.text.format.DateFormat.format("MMM dd, yyyy", timestamp)
        textSelectedDate.text = "Selected: $formattedDate"
    }
}

