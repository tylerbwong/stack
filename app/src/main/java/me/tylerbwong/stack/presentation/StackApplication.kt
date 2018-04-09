package me.tylerbwong.stack.presentation

import android.app.Application
import me.tylerbwong.stack.BuildConfig
import timber.log.Timber

class StackApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        }
    }
}