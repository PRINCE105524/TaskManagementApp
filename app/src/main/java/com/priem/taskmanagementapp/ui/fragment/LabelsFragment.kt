package com.priem.taskmanagementapp.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class LabelsFragment : Fragment() {

    private lateinit var chipGroupLabels: ChipGroup
    private lateinit var fabAddLabel: FloatingActionButton
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_labels, container, false)

        chipGroupLabels = view.findViewById(R.id.chipGroupLabels)
        fabAddLabel = view.findViewById(R.id.fabAddLabel)

        setupObservers()

        fabAddLabel.setOnClickListener {
            showAddLabelDialog()
        }

        taskViewModel.loadLabels()

        return view
    }

    private fun setupObservers() {
        taskViewModel.availableLabels.observe(viewLifecycleOwner) { labels ->
            setupChips(labels.map { it.name })
        }
    }

    private fun setupChips(labelNames: List<String>) {
        chipGroupLabels.removeAllViews()

        for (label in labelNames) {
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

    private fun showAddLabelDialog() {
        val input = EditText(requireContext())
        input.hint = "Enter new label"

        AlertDialog.Builder(requireContext())
            .setTitle("Add Label")
            .setView(input)
            .setPositiveButton("Add") { dialog, _ ->
                val newLabelName = input.text.toString().trim()
                if (newLabelName.isNotEmpty() && taskViewModel.availableLabels.value?.none { it.name == newLabelName } == true) {
                    lifecycleScope.launch {
                        taskViewModel.insertLabel(newLabelName)
                    }
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Label already exists", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}




