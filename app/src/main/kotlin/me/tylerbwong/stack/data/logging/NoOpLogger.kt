package me.tylerbwong.stack.data.logging

class NoOpLogger : Logger {
    override var isLoggingEnabled: Boolean
        get() = false
        set(_) {
            // No-op
        }

    override fun logEvent(eventName: String, vararg params: Pair<String, String>) {
        // No-op
    }
}
