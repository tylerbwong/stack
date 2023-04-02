package me.tylerbwong.stack.base.logger

import me.tylerbwong.stack.data.logging.Logger

class NoOpLogger : Logger {
    override fun logEvent(eventName: String, vararg params: Pair<String, String>) {
        // No-op
    }
}
