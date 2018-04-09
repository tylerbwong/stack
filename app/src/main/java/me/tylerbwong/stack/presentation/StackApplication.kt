package me.tylerbwong.stack.presentation

import android.app.Application
import io.reactivex.plugins.RxJavaPlugins
import me.tylerbwong.stack.BuildConfig
import timber.log.Timber

class StackApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setErrorHandler(Timber::e)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
