package me.tylerbwong.stack.data.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics
import me.tylerbwong.stack.BuildConfig
import timber.log.Timber

object Logger {
    fun init() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.plant(CrashlyticsTree())
    }
}
