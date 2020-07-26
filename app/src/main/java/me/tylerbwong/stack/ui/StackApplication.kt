package me.tylerbwong.stack.ui

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.HiltAndroidApp
import me.tylerbwong.stack.data.logging.Logger
import me.tylerbwong.stack.ui.theme.ThemeManager
import me.tylerbwong.stack.ui.utils.CoilInitializer
import javax.inject.Inject

@HiltAndroidApp
class StackApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {

        if (ProcessPhoenix.isPhoenixProcess(this)) {
            return
        }

        super.onCreate()

        ApplicationWrapper.init(this)

        FirebaseApp.initializeApp(this)

        ThemeManager.init(this)

        CoilInitializer.init(this)

        Logger.init()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}
