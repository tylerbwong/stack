package me.tylerbwong.stack.ui

import android.app.Application
import com.google.firebase.FirebaseApp
import com.jakewharton.processphoenix.ProcessPhoenix
import me.tylerbwong.stack.data.logging.Logger
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.work.Work
import me.tylerbwong.stack.ui.theme.ThemeManager
import me.tylerbwong.stack.ui.utils.CoilInitializer

class StackApplication : Application() {
    override fun onCreate() {

        if (ProcessPhoenix.isPhoenixProcess(this)) {
            return
        }

        super.onCreate()

        ApplicationWrapper.init(this)

        FirebaseApp.initializeApp(this)

        ThemeManager.init(this)

        StackDatabase.init(this)

        CoilInitializer.init(this)

        Logger.init()

        Work.schedule()
    }
}
