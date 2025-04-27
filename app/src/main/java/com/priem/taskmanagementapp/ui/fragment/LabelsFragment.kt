package com.priem.taskmanagementapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.viewmodel.TaskViewModel

class LabelsFragment : Fragment() {

    private lateinit var chipGroupLabels: ChipGroup
    private lateinit var taskViewModel: TaskViewModel

    private val availableLabels = listOf("Bug", "Feature", "Urgent", "Improvement", "Meeting", "Idea")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_labels, container, false)

        chipGroupLabels = view.findViewById(R.id.chipGroupLabels)

        setupChips()

        return view
    }

    private fun setupChips() {
        chipGroupLabels.removeAllViews()

        for (label in availableLabels) {
            val chip = Chip(requireContext())
            chip.text = label
            chip.isCheckable = true

            chip.setOnCheckedChangeListener { _, _ ->
                saveSelectedLabels()
            }

            chipGroupLabels.addView(chip)
        }
    }

    private fun saveSelectedLabels() {
        val selectedLabels = chipGroupLabels.children
            .filterIsInstance<Chip>()
            .filter { it.isChecked }
            .map { it.text.toString() }
            .toList()

        taskViewModel.setLabels(selectedLabels)
    }
}

