package me.tylerbwong.stack.ui

import android.content.Context

@Suppress("StaticFieldLeak") // Application context is safe
object ApplicationWrapper {
    lateinit var context: Context
        private set

    fun init(context: Context) {
        this.context = context
    }
}
