package me.tylerbwong.stack.data.updater

import android.app.Activity

class NoOpAppUpdater : AppUpdater {
    override fun checkForUpdate(activity: Activity) {
        // No-op
    }

    override fun checkForPendingInstall(
        onDownloadFinished: () -> Unit,
        onDownloadFailed: () -> Unit
    ) {
        // No-op
    }

    override fun completeUpdate() {
        // No-op
    }

    override fun unregisterListener(activity: Activity) {
        // No-op
    }
}
