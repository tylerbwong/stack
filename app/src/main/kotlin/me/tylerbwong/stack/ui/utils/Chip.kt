package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.view.View
import com.google.android.material.chip.Chip
import me.tylerbwong.stack.R

fun Context.createChip(
    label: String,
    onClick: ((View) -> Unit)? = null,
): Chip {
    return Chip(this).apply {
        chipBackgroundColor = null
        elevation = 0f
        stateListAnimator = null
        setChipStrokeWidthResource(R.dimen.chip_stroke_width)
        setChipStrokeColorResource(R.color.secondaryTextColor)
        text = label
        onClick?.let { setThrottledOnClickListener(it) }
    }
}
