package me.tylerbwong.stack.ui.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ViewHolderItemDecoration(
    private val spacing: Int,
    private val removeSideSpacing: Boolean = false,
    private val removeTopSpacing: Boolean = false
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        view.let {
            val position = parent.getChildAdapterPosition(it)
            val size = parent.adapter?.itemCount ?: 0

            outRect.apply {
                if (position == 0) {
                    top = spacing / 4
                }

                if (position == size - 1 || removeTopSpacing) {
                    bottom = spacing
                }

                if (!removeTopSpacing) {
                    top = spacing
                }

                if (!removeSideSpacing) {
                    left = spacing
                    right = spacing
                }
            }
        }
    }
}
