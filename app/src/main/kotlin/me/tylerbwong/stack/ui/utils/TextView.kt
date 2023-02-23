package me.tylerbwong.stack.ui.utils

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

internal fun TextView.renderSelectedState(
    @ColorRes selectedColor: Int,
    value: Int,
    isSelected: Boolean
) {
    @ColorInt val color = if (isSelected) {
        ContextCompat.getColor(context, selectedColor)
    } else {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
        typedValue.data
    }
    setTextColor(color)
    text = value.toLong().format()
    compoundDrawables.forEach {
        if (it != null) {
            it.mutate().colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }
}
