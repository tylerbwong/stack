package me.tylerbwong.stack

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflateWithoutAttaching(@LayoutRes resId: Int): View? =
        LayoutInflater.from(context).inflate(resId, this, false)

fun NOOP(message: String = "No operation needed."): Nothing = TODO(message)