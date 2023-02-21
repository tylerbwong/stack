package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import com.google.android.material.chip.Chip

fun Context.createChip(
    label: String,
    @ColorRes strokeColorRes: Int? = null,
    onClick: ((View) -> Unit)? = null,
): Chip {
    return Chip(this).apply {
        elevation = 0f
        stateListAnimator = null
        strokeColorRes?.let { setChipStrokeColorResource(it) }
        text = label
        onClick?.let { setThrottledOnClickListener(it) }
    }
}
