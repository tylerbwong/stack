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
                firebaseCrashlytics.setCustomKey(KEY_PRIORITY, priority)
                firebaseCrashlytics.setCustomKey(KEY_TAG, tag.orEmpty())
                firebaseCrashlytics.setCustomKey(KEY_MESSAGE, message)

                if (t == null) {
                    firebaseCrashlytics.recordException(IllegalStateException(message))
                } else {
                    firebaseCrashlytics.recordException(t)
                }
            }
        }
    }

    companion object {
        private const val KEY_PRIORITY = "priority"
        private const val KEY_TAG = "tag"
        private const val KEY_MESSAGE = "message"
    }
}
