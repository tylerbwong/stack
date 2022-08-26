package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import com.google.android.material.chip.Chip
import me.tylerbwong.stack.R

fun Context.createChip(
    label: String,
    @ColorRes strokeColorRes: Int = R.color.secondaryTextColor,
    onClick: ((View) -> Unit)? = null,
): Chip {
    return Chip(this).apply {
        elevation = 0f
        stateListAnimator = null
        setChipStrokeColorResource(strokeColorRes)
        text = label
        onClick?.let { setThrottledOnClickListener(it) }
    }
}
