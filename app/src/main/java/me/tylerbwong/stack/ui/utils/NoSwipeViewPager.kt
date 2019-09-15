package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoSwipeViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    var isSwipeable = true

    override fun onInterceptTouchEvent(ev: MotionEvent?) = if (isSwipeable) {
        super.onInterceptTouchEvent(ev)
    } else {
        false
    }

    @Suppress("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?) = if (isSwipeable) {
        super.onTouchEvent(ev)
    } else {
        false
    }
}
