package me.tylerbwong.stack.ui.questions.detail

import androidx.recyclerview.widget.RecyclerView

/*
 * Temporary workaround for bug in IR with anonymous classes.
 * https://youtrack.jetbrains.com/issue/KT-41450
 */
class QuestionDetailOnScrollListener(
    private val onScrolled: (dy: Int) -> Unit
) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) = onScrolled(dy)
}
