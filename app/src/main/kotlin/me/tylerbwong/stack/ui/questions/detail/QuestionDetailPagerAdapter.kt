package me.tylerbwong.stack.ui.questions.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.tylerbwong.stack.ui.questions.detail.post.PostAnswerFragment

class QuestionDetailPagerAdapter(
    activity: FragmentActivity,
    private val questionId: Int
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> QuestionDetailFragment.newInstance(questionId)
            1 -> PostAnswerFragment.newInstance(questionId)
            else -> throw IllegalStateException("No fragment for position: $position")
        }
    }

    override fun getItemCount() = 2
}
