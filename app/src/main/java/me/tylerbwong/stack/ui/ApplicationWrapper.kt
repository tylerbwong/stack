package me.tylerbwong.stack.ui

import android.content.Context
import me.tylerbwong.stack.data.di.DaggerDataComponent
import me.tylerbwong.stack.data.di.DataComponent
import me.tylerbwong.stack.ui.di.DaggerUiComponent
import me.tylerbwong.stack.ui.di.UiComponent

@Suppress("StaticFieldLeak") // Application context is safe
object ApplicationWrapper {
    lateinit var context: Context
        private set

    lateinit var dataComponent: DataComponent
    lateinit var uiComponent: UiComponent

    fun init(context: Context) {
        this.context = context
        dataComponent = DaggerDataComponent.create()
        uiComponent = DaggerUiComponent.create()
    }
}
