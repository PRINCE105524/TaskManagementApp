package com.priem.taskmanagementapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priem.taskmanagementapp.R
import com.priem.taskmanagementapp.data.model.FollowerData

class FollowersAdapter(private val followers: List<FollowerData>) :
    RecyclerView.Adapter<FollowersAdapter.FollowerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_followers, parent, false)
        return FollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val follower = followers[position]
        holder.bind(follower)
    }

    override fun getItemCount(): Int = followers.size

    class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageAvatar: ImageView = itemView.findViewById(R.id.imageAvatar)
        private val textName: TextView = itemView.findViewById(R.id.textName)

        fun bind(follower: FollowerData) {
            textName.text = follower.name
            // Dummy avatar, you can load real image if available later
            imageAvatar.setImageResource(R.drawable.ic_avatar_placeholder)
        }
    }
}
