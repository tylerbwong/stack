package me.tylerbwong.stack.ui.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfilePagerAdapter(
    activity: FragmentActivity,
    private val userId: Int,
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfilePageFragment.newInstance(userId, ProfileViewModel.ProfilePage.QUESTIONS)
            1 -> ProfilePageFragment.newInstance(userId, ProfileViewModel.ProfilePage.ANSWERS)
            else -> throw IllegalStateException("No fragment for position: $position")
        }
    }

    override fun getItemCount() = 2
}
