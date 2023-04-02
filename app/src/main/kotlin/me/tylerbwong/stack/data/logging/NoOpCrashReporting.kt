package me.tylerbwong.stack.data.logging

class NoOpCrashReporting : CrashReporting {
    override var isCrashReportingEnabled
        get() = false
        set(_) {
            // No-op
        }

    override fun initialize() {
        // No-op
    }
}
