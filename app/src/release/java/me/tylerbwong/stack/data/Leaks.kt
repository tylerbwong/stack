package me.tylerbwong.stack.data

object Leaks {
    var isLeakCanaryEnabled = false

    fun initialize(isEnabled: Boolean = isLeakCanaryEnabled) {
        // no-op
    }
}

