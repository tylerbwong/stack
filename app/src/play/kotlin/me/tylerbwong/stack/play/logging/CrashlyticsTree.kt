package me.tylerbwong.stack.play.logging

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import javax.inject.Inject

class CrashlyticsTree @Inject constructor() : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.DEBUG, Log.INFO, Log.VERBOSE -> return
            else -> {
                val firebaseCrashlytics = FirebaseCrashlytics.getInstance()
                if (t == null) {
                    firebaseCrashlytics.recordException(IllegalStateException(message))
                } else {
                    firebaseCrashlytics.recordException(t)
                }
            }
        }
    }
}
