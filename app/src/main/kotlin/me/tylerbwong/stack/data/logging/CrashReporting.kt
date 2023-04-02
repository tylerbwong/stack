package me.tylerbwong.stack.data.logging

interface CrashReporting {
    var isCrashReportingEnabled: Boolean
    fun initialize()
}
