package me.tylerbwong.stack.ui.questions.detail

import androidx.viewpager2.widget.ViewPager2

/*
 * Temporary workaround for bug in IR with anonymous classes.
 * https://youtrack.jetbrains.com/issue/KT-41450
 */
class QuestionDetailPageChangeCallback(
    private val onPageSelected: (Int) -> Unit
) : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) = onPageSelected.invoke(position)
}
