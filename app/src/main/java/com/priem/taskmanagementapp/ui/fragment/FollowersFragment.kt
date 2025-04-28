package com.priem.taskmanagementapp.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.database.TaskDatabase
import com.priem.taskmanagementapp.data.entity.User
import com.priem.taskmanagementapp.repository.TaskRepository
import com.priem.taskmanagementapp.ui.adapter.FollowerAdapter
import com.priem.taskmanagementapp.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class FollowersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddUser: FloatingActionButton
    private lateinit var adapter: FollowerAdapter
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var repository: TaskRepository

    private val selectedUserIds = mutableSetOf<Long>()

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
    }

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

        fabAddUser = view.findViewById(R.id.fabAddUser)

        val dao = TaskDatabase.getDatabase(requireContext()).taskDao()
        repository = TaskRepository(dao)

        fabAddUser.setOnClickListener {
            showAddUserDialog()
        }

        loadUsers()

        return view
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            repository.getAllUsers().observe(viewLifecycleOwner) { users ->
                adapter = FollowerAdapter(users, selectedUserIds) { updatedSelectedIds ->
                    taskViewModel.setFollowers(updatedSelectedIds.toList())
                }
                recyclerView.adapter = adapter
            }
        }
    }

    private fun showAddUserDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_user, null)
        val imageAvatar = dialogView.findViewById<ImageView>(R.id.imageAvatar)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextPhone = dialogView.findViewById<EditText>(R.id.editTextPhone)

        var selectedAvatarPath: String? = null

        imageAvatar.setOnClickListener {
            pickImageFromGallery { path ->
                selectedAvatarPath = path
                imageAvatar.setImageURI(android.net.Uri.parse(path))
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle("New User")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val name = editTextName.text.toString().trim()
                val phone = editTextPhone.text.toString().trim()

                if (name.isNotEmpty()) {
                    addUser(name, phone, selectedAvatarPath)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun addUser(name: String, phone: String, avatarUrl: String?) {
        lifecycleScope.launch {
            val user = User(
                name = name,
                phoneNumber = phone,
                avatarUrl = avatarUrl
            )
            repository.insertUser(user)
        }
    }

    private fun pickImageFromGallery(onImagePicked: (String) -> Unit) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)

        imagePickCallback = onImagePicked
    }

    private var imagePickCallback: ((String) -> Unit)? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            val uri = data?.data
            uri?.let {
                imagePickCallback?.invoke(it.toString())
            }
        }
    }




}