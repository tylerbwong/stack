package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import java.util.*

fun ViewGroup.inflateWithoutAttaching(@LayoutRes resId: Int): View =
        LayoutInflater.from(context).inflate(resId, this, false)

fun String.toHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
} else {
    @Suppress("DEPRECATION")
    Html.fromHtml(this)
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
    val divideBy = e?.key ?: 0
    val suffix = e?.value

    val truncated = this / (divideBy / 10) //the number part of the output times 10
    val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
    return if (hasDecimal) "${truncated / 10.0}$suffix" else "${truncated / 10}$suffix"
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}
