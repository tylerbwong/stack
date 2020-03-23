package me.tylerbwong.stack.ui.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.tylerbwong.stack.R
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

inline fun EditText.addThrottledTextChangedListener(
    delay: Long = 300,
    crossinline afterTextChanged: (String) -> Unit
): TextWatcher {
    val textWatcher = object : TextWatcher {
        var lastInput = ""
        var debounceJob: Job? = null
        val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        override fun afterTextChanged(s: Editable?) {
            val newInput = s?.toString()?.trim()
            if (newInput != null) {
                debounceJob?.cancel()
                if (lastInput != newInput) {
                    lastInput = newInput
                    debounceJob = uiScope.launch {
                        delay(delay)
                        afterTextChanged(newInput)
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // no-op
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // no-op
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
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
