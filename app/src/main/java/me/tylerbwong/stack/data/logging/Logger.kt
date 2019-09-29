package me.tylerbwong.stack.data.logging

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import me.tylerbwong.stack.BuildConfig
import timber.log.Timber

object Logger {
    fun init(context: Context) {
        val applicationContext = context.applicationContext
        val crashlytics = CrashlyticsCore.Builder()
            .disabled(BuildConfig.DEBUG)
            .build()
        Fabric.with(applicationContext, Crashlytics.Builder().core(crashlytics).build())

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.plant(CrashlyticsTree())
    }
}
