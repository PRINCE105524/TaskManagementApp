package com.priem.taskmanagementapp.ui.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.entity.User

class FollowerAdapter(
    private val users: List<User>,
    private val selectedUserIds: MutableSet<Long>,
    private val onSelectionChanged: (Set<Long>) -> Unit
) : RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_follower, parent, false)
        return FollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, selectedUserIds, onSelectionChanged)
    }

    override fun getItemCount() = users.size

    class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBoxFollower: CheckBox = itemView.findViewById(R.id.checkBoxFollower)
        private val textName: TextView = itemView.findViewById(R.id.textFollowerName)
        private val textPhone: TextView = itemView.findViewById(R.id.textFollowerPhone)
        private val imageAvatar: ImageView = itemView.findViewById(R.id.imageAvatar)

        fun bind(
            user: User,
            selectedUserIds: MutableSet<Long>,
            onSelectionChanged: (Set<Long>) -> Unit
        ) {
            textName.text = user.name
            textPhone.text = user.phoneNumber
            checkBoxFollower.isChecked = selectedUserIds.contains(user.userId)

            // Load avatar
            if (!user.avatarUrl.isNullOrEmpty()) {
                imageAvatar.setImageBitmap(BitmapFactory.decodeFile(user.avatarUrl))
            } else {
                imageAvatar.setImageResource(R.drawable.ic_avatar_placeholder)
            }

            itemView.setOnClickListener {
                checkBoxFollower.isChecked = !checkBoxFollower.isChecked
                toggleSelection(user.userId, selectedUserIds, onSelectionChanged)
            }

            checkBoxFollower.setOnClickListener {
                toggleSelection(user.userId, selectedUserIds, onSelectionChanged)
            }
        }

        private fun toggleSelection(
            userId: Long,
            selectedUserIds: MutableSet<Long>,
            onSelectionChanged: (Set<Long>) -> Unit
        ) {
            if (selectedUserIds.contains(userId)) {
                selectedUserIds.remove(userId)
            } else {
                selectedUserIds.add(userId)
            }
            onSelectionChanged(selectedUserIds)
        }
    }
}