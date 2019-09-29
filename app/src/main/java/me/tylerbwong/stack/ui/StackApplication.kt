package me.tylerbwong.stack.ui

import android.app.Application
import me.tylerbwong.stack.data.logging.Logger
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.ui.theme.ThemeManager
import me.tylerbwong.stack.ui.utils.markdown.Markdown

class StackApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ApplicationWrapper.init(this)

        ThemeManager.init(this)

        StackDatabase.init(this)

        Markdown.init(this)

        Logger.init(this)
    }
}
