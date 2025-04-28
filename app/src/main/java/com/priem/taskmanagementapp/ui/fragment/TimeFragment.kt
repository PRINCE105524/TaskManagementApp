package com.priem.taskmanagementapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.viewmodel.TaskViewModel

class TimeFragment : Fragment() {

    private lateinit var buttonPickTime: Button
    private lateinit var textSelectedTime: TextView
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_time, container, false)

        buttonPickTime = view.findViewById(R.id.buttonPickTime)
        textSelectedTime = view.findViewById(R.id.textSelectedTime)

        buttonPickTime.setOnClickListener {
            openTimePicker()
        }

        // Pre-fill if time already selected
        taskViewModel.taskDueTime.value?.let { timeText ->
            textSelectedTime.text = "Selected: $timeText"
        }

        return view
    }

    private fun openTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H) // or CLOCK_24H
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Time")
            .build()

        picker.show(parentFragmentManager, "timePicker")

        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour
            val minute = picker.minute

            val formattedTime = String.format("%02d:%02d", hour, minute)

            taskViewModel.setDueTime(formattedTime)
            textSelectedTime.text = "Selected: $formattedTime"
        }
    }
}

