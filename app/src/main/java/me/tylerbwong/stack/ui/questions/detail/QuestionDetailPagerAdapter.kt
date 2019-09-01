package me.tylerbwong.stack.ui.questions.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import me.tylerbwong.stack.ui.questions.detail.submit.SubmitAnswerFragment

class QuestionDetailPagerAdapter(
        manager: FragmentManager,
        private val questionId: Int
) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    internal var isInAnswerMode: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> QuestionDetailFragment.newInstance(questionId)
            1 -> SubmitAnswerFragment.newInstance()
            else -> throw IllegalStateException("No fragment for position: $position")
        }
    }

    override fun getCount() = if (isInAnswerMode) {
        2
    } else {
        1
    }
}
