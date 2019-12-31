package me.tylerbwong.stack.ui.home

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HeaderItemDecoration(private val headerView: View) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            headerView.measure(
                View.MeasureSpec.makeMeasureSpec(parent.measuredWidth, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(parent.measuredHeight, View.MeasureSpec.AT_MOST)
            )
            outRect.top = headerView.measuredHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val topChild = parent.getChildAt(0)
        if (parent.getChildAdapterPosition(topChild) == 0) {
            headerView.layout(parent.left, 0, parent.right, headerView.measuredHeight)

            c.save()
            c.translate(0f, topChild.top.toFloat() - headerView.measuredHeight)
            headerView.draw(c)
            c.restore()
        }
    }
}
