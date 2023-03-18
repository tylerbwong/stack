package me.tylerbwong.stack.ui.utils

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import androidx.core.text.getSpans

fun Spanned.replaceUrlSpans(): Spanned {
    val newSpanned = SpannableStringBuilder(this)
    getSpans<URLSpan>().forEach {
        val start = getSpanStart(it)
        val end = getSpanEnd(it)
        newSpanned.removeSpan(it)
        newSpanned.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) = view.context.launchUrl(it.url)
            },
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return newSpanned
}
