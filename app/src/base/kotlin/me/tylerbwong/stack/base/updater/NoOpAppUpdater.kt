package me.tylerbwong.stack.base.updater

import me.tylerbwong.stack.data.updater.AppUpdater
import me.tylerbwong.stack.ui.MainActivity

class NoOpAppUpdater : AppUpdater {
    override fun checkForUpdate(activity: MainActivity) {
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

    override fun unregisterListener() {
        // No-op
    }
}
