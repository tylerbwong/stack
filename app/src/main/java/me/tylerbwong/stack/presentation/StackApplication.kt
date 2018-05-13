package me.tylerbwong.stack.presentation

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import io.reactivex.plugins.RxJavaPlugins
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.persistence.StackDatabase
import timber.log.Timber

class StackApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        StackDatabase.init(this)

        RxJavaPlugins.setErrorHandler(Timber::e)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
    }
}
