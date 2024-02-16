package me.tylerbwong.stack.data.updater

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest

interface AppUpdater {
    fun checkForUpdate(
        checkForPendingInstall: () -> Unit,
        activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
    )
    fun checkForPendingInstall(onDownloadFinished: () -> Unit, onDownloadFailed: () -> Unit)
    fun completeUpdate()
    fun unregisterListener()
}
