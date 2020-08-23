package me.tylerbwong.stack.ui.utils

val String.withHttps: String
    get() = if (!this.contains("http")) {
        "https:$this"
    } else {
        this
    }
