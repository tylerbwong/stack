package me.tylerbwong.stack.ui

import android.app.Application
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.ui.theme.ThemeManager
import me.tylerbwong.stack.ui.utils.MarkdownUtils
import timber.log.Timber

class StackApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ThemeManager.init(this)

        StackDatabase.init(this)

        MarkdownUtils.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
