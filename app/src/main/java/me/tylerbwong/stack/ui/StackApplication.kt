package me.tylerbwong.stack.ui

import android.app.Application
import com.google.firebase.FirebaseApp
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.HiltAndroidApp
import me.tylerbwong.stack.data.logging.Logger
import me.tylerbwong.stack.data.work.Work
import me.tylerbwong.stack.ui.theme.ThemeManager
import me.tylerbwong.stack.ui.utils.CoilInitializer

@HiltAndroidApp
class StackApplication : Application() {
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

        Work.schedule()
    }
}
