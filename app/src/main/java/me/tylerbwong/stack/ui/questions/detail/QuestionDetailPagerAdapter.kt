package me.tylerbwong.stack.ui.questions.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import me.tylerbwong.stack.ui.questions.detail.post.PostAnswerFragment

class QuestionDetailPagerAdapter(
    manager: FragmentManager,
    private val questionId: Int
) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> QuestionDetailFragment.newInstance(questionId)
            1 -> PostAnswerFragment.newInstance(questionId)
            else -> throw IllegalStateException("No fragment for position: $position")
        }
    }

    override fun getCount() = 2
}
