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

    private var prefillHour = 12
    private var prefillMinute = 0
    private var prefillIsPm = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        // Extract hour and minute if already selected
        taskViewModel.taskDueTime.value?.let { timeText ->
            val parts = timeText.split(" ", ":")
            if (parts.size == 3) {
                prefillHour = parts[0].toIntOrNull() ?: 12
                prefillMinute = parts[1].toIntOrNull() ?: 0
                prefillIsPm = parts[2].equals("PM", ignoreCase = true)
            }
        }
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

        // Pre-fill text view
        taskViewModel.taskDueTime.value?.let { timeText ->
            textSelectedTime.text = "Selected: $timeText"
        }

        return view
    }

    private fun openTimePicker() {
        // Convert to 24-hour format for MaterialTimePicker
        val pickerHour = if (prefillIsPm && prefillHour < 12) prefillHour + 12 else if (!prefillIsPm && prefillHour == 12) 0 else prefillHour

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(pickerHour)
            .setMinute(prefillMinute)
            .setTitleText("Select Time")
            .build()

        picker.show(parentFragmentManager, "timePicker")

        picker.addOnPositiveButtonClickListener {
            val hour24 = picker.hour
            val minute = picker.minute

            // Convert to 12-hour format with AM/PM
            val isPm = hour24 >= 12
            val hour12 = when {
                hour24 == 0 -> 12
                hour24 > 12 -> hour24 - 12
                else -> hour24
            }

            val amPm = if (isPm) "PM" else "AM"
            val formattedTime = String.format("%02d:%02d %s", hour12, minute, amPm)
            val formattedTimeExcludeAM_PM = String.format("%02d:%02d", hour12, minute)


            taskViewModel.setDueTime(formattedTimeExcludeAM_PM)
            textSelectedTime.text = "Selected: $formattedTime"
        }
    }
}

