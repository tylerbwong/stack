package me.tylerbwong.stack.play.logging

import android.system.ErrnoException
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import java.io.EOFException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class CrashlyticsTree @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics,
) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.DEBUG, Log.INFO, Log.VERBOSE -> return
            else -> {
                if (t == null) {
                    firebaseCrashlytics.recordException(IllegalStateException(message))
                } else {
                    if (t::class !in filterableExceptionTypes) {
                        firebaseCrashlytics.recordException(t)
                    }
                }
            }
        }
    }

    companion object {
        private val filterableExceptionTypes = setOf(
            UnknownHostException::class,
            EOFException::class,
            SocketException::class,
            SocketTimeoutException::class,
            ErrnoException::class,
            CancellationException::class,
        )
    }
}
