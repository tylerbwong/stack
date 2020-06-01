package me.tylerbwong.stack

import android.app.Application
import me.tylerbwong.stack.ui.ApplicationWrapper

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationWrapper.init(this)
    }
}
