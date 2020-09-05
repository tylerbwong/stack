package me.tylerbwong.stack.ui.utils

import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import me.tylerbwong.stack.R
import kotlin.math.abs

private const val MINIMUM_CLICK_INTERVAL = 1000

fun View.setThrottledOnClickListener(listener: (View) -> Unit) {
    setOnClickListener(
        object : View.OnClickListener {
            private var lastClickedTime = 0L

            override fun onClick(view: View) {
                val currentTime = System.currentTimeMillis()

                if (abs(currentTime - lastClickedTime) > MINIMUM_CLICK_INTERVAL) {
                    listener(view)
                    lastClickedTime = currentTime
                }
            }
        }
    )
}

fun View.showSnackbar(
    @StringRes messageId: Int,
    @StringRes actionTextId: Int? = null,
    @Duration duration: Int = Snackbar.LENGTH_INDEFINITE,
    shouldAnchorView: Boolean = false,
    onActionClicked: ((View) -> Unit)? = null
): Snackbar {
    val snackbar = Snackbar.make(this, messageId, duration)

    if (shouldAnchorView) {
        snackbar.anchorView = this
    }

    if (actionTextId != null && onActionClicked != null) {
        snackbar.setAction(actionTextId, onActionClicked)
            .setActionTextColor(ContextCompat.getColor(context, R.color.snackbarActionTextColor))
    }

    return snackbar.also { it.show() }
}
