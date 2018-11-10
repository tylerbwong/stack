package me.tylerbwong.stack.presentation.utils

import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import java.util.TreeMap

private val specialChars = mapOf(
        "&lt;" to "<",
        "&gt;" to ">",
        "&quot;" to "\"",
        "&nbsp;" to " ",
        "&amp;" to "&",
        "&apos;" to "'",
        "&#39;" to "'",
        "&#40;" to "(",
        "&#41;" to ")",
        "&#215;" to "×"
)

fun ViewGroup.inflateWithoutAttaching(@LayoutRes resId: Int): View =
        LayoutInflater.from(context).inflate(resId, this, false)

fun String.toHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
} else {
    @Suppress("DEPRECATION")
    Html.fromHtml(this)
}

fun String.stripSpecials(): String {
    var result = this
    specialChars.forEach { (key, value) ->
         result = result.replace(key, value)
    }
    return result
}

private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickable = object : ClickableSpan() {
        override fun onClick(view: View) {
            launchCustomTab(view.context, span.url)
        }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
}

fun TextView.setHtml(html: String?) {
    html?.let {
        val sequence = it.toHtml()
        val strBuilder = SpannableStringBuilder(sequence)
        val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span)
        }
        text = strBuilder
        movementMethod = LinkMovementMethod.getInstance()
    }
}

private val suffixes = TreeMap<Long, String>().apply {
    put(1_000L, "k")
    put(1_000_000L, "M")
    put(1_000_000_000L, "G")
    put(1_000_000_000_000L, "T")
    put(1_000_000_000_000_000L, "P")
    put(1_000_000_000_000_000_000L, "E")
}

fun Long.format(): String {
    //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
    if (this == Long.MIN_VALUE) return (Long.MIN_VALUE + 1).format()
    if (this < 0) return "-" + (-this).format()
    if (this < 1000) return this.toString() //deal with easy case

    val e = suffixes.floorEntry(this)
    val divideBy = e.key
    val suffix = e.value

    val truncated = this / (divideBy / 10) //the number part of the output times 10
    val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
    return if (hasDecimal) "${truncated / 10.0}$suffix" else "${truncated / 10}$suffix"
}
