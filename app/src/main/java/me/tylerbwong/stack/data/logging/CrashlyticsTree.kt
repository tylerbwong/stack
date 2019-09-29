package me.tylerbwong.stack.data.logging

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.DEBUG, Log.INFO, Log.VERBOSE -> return
            else -> {
                Crashlytics.setInt(KEY_PRIORITY, priority)
                Crashlytics.setString(KEY_TAG, tag)
                Crashlytics.setString(KEY_MESSAGE, message)

                if (t == null) {
                    Crashlytics.logException(IllegalStateException(message))
                } else {
                    Crashlytics.logException(t)
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
