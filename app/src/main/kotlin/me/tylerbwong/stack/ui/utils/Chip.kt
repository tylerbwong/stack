package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import me.tylerbwong.stack.R

fun Context.createChip(
    label: String,
    @ColorRes backgroundColorRes: Int = R.color.viewBackgroundColor,
    @ColorRes strokeColorRes: Int = R.color.secondaryTextColor,
    @DimenRes strokeWidthRes: Int = R.dimen.chip_stroke_width,
    onClick: ((View) -> Unit)? = null,
): Chip {
    return Chip(this).apply {
        chipBackgroundColor = ColorStateList.valueOf(
            ContextCompat.getColor(context, backgroundColorRes)
        )
        elevation = 0f
        stateListAnimator = null
        setChipStrokeWidthResource(strokeWidthRes)
        setChipStrokeColorResource(strokeColorRes)
        text = label
        onClick?.let { setThrottledOnClickListener(it) }
    }
}
