package me.tylerbwong.stack.ui

import android.app.Application
import com.facebook.stetho.Stetho
import io.reactivex.plugins.RxJavaPlugins
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.ui.theme.ThemeManager
import timber.log.Timber

class StackApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ThemeManager.init(this)

        StackDatabase.init(this)

        RxJavaPlugins.setErrorHandler {
            when (it) {
                is Error -> throw it
                else -> Timber.e(it)
            }
        }

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
    }
}
