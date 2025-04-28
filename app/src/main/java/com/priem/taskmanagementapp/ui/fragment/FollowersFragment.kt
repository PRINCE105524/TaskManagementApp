package com.priem.taskmanagementapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.database.TaskDatabase
import com.priem.taskmanagementapp.repository.TaskRepository
import com.priem.taskmanagementapp.ui.adapter.FollowerAdapter
import com.priem.taskmanagementapp.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class FollowersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FollowerAdapter
    private lateinit var taskViewModel: TaskViewModel
    private val selectedUserIds = mutableSetOf<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_followers, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewFollowers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Load users from database
        val dao = TaskDatabase.getDatabase(requireContext()).taskDao()
        val repository = TaskRepository(dao)

        lifecycleScope.launch {
            repository.getAllUsers().observe(viewLifecycleOwner) { users ->
                adapter = FollowerAdapter(users, selectedUserIds) { updatedSelectedIds ->
                    taskViewModel.setFollowers(updatedSelectedIds.toList())
                }
                recyclerView.adapter = adapter
            }
        }

        return view
    }
}
