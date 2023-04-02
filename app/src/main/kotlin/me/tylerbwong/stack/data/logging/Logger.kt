package me.tylerbwong.stack.data.logging

interface Logger {
    fun logEvent(eventName: String, vararg params: Pair<String, String>)
}
