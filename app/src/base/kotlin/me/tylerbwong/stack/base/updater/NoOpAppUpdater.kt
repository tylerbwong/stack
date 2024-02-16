package me.tylerbwong.stack.base.updater

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import me.tylerbwong.stack.data.updater.AppUpdater

class NoOpAppUpdater : AppUpdater {
    override fun checkForUpdate(
        checkForPendingInstall: () -> Unit,
        activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
    ) {
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
