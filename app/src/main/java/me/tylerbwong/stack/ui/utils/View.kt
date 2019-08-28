package me.tylerbwong.stack.ui.utils

import android.view.View
import kotlin.math.abs

private const val MINIMUM_CLICK_INTERVAL = 1000

fun View.setThrottledOnClickListener(listener: (View) -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickedTime = 0L

        override fun onClick(view: View) {
            val currentTime = System.currentTimeMillis()

            if (abs(currentTime - lastClickedTime) > MINIMUM_CLICK_INTERVAL) {
                listener(view)
                lastClickedTime = currentTime
            }
        }
    })
}
