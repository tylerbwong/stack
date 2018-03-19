package me.tylerbwong.stack

import android.os.Build
import android.support.annotation.LayoutRes
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflateWithoutAttaching(@LayoutRes resId: Int): View? =
        LayoutInflater.from(context).inflate(resId, this, false)

fun NOOP(message: String = "No operation needed."): Nothing = TODO(message)

fun String.toHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
} else {
    @Suppress("DEPRECATION")
    Html.fromHtml(this)
}
