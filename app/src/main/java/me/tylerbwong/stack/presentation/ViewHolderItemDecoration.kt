package me.tylerbwong.stack.presentation

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ViewHolderItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
            outRect: Rect?,
            view: View?,
            parent: RecyclerView?,
            state: RecyclerView.State?
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        view?.let {
            val position = parent?.getChildAdapterPosition(it)
            val size = parent?.adapter?.itemCount ?: 0

            outRect?.apply {
                top = spacing
                left = spacing
                right = spacing

                if (position == size - 1) {
                    bottom = spacing
                }
            }
        }
    }
}