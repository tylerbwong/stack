package me.tylerbwong.stack.data.logging

interface Logger {
    var isLoggingEnabled: Boolean
    fun logEvent(eventName: String, vararg params: Pair<String, String>)
}
