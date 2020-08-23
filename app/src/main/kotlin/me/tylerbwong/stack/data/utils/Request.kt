package me.tylerbwong.stack.data.utils

import okhttp3.Request

val Request.isPost get() = method.equals("post", ignoreCase = true)
