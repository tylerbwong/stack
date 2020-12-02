package me.tylerbwong.stack.data.logging

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {
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
