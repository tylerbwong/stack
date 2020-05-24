package me.tylerbwong.stack.ui

import android.content.Context
import me.tylerbwong.stack.ui.di.DaggerStackComponent
import me.tylerbwong.stack.ui.di.StackComponent

@Suppress("StaticFieldLeak") // Application context is safe
object ApplicationWrapper {
    lateinit var context: Context
        private set

    lateinit var stackComponent: StackComponent

    fun init(context: Context) {
        this.context = context
        stackComponent = DaggerStackComponent.create()
    }
}
