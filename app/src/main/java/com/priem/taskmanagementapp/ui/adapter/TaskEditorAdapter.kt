package com.priem.taskmanagementapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.priem.taskmanagementapp.ui.fragment.*

class TaskEditorAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DetailsFragment()
            1 -> LabelsFragment()
            2 -> CalendarFragment()
            3 -> TimeFragment()
            4 -> FollowersFragment()
            5 -> PriorityFragment()
            6 -> AttachmentsFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
