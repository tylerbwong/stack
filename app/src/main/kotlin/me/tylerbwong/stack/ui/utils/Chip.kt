package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import me.tylerbwong.stack.R

fun Context.createChip(
    label: String,
    onClick: ((View) -> Unit)? = null,
): Chip {
    return Chip(this).apply {
        val viewBackgroundColor = ContextCompat.getColor(context, R.color.viewBackgroundColor)
        chipBackgroundColor = ColorStateList.valueOf(viewBackgroundColor)
        elevation = 0f
        stateListAnimator = null
        setChipStrokeWidthResource(R.dimen.chip_stroke_width)
        setChipStrokeColorResource(R.color.secondaryTextColor)
        text = label
        onClick?.let { setThrottledOnClickListener(it) }
    }
}
