package me.tylerbwong.stack.data.updater

import android.app.Activity

interface AppUpdater {
    fun checkForUpdate(activity: Activity)
    fun checkForPendingInstall(onDownloadFinished: () -> Unit, onDownloadFailed: () -> Unit)
    fun completeUpdate()
    fun unregisterListener(activity: Activity)

    companion object {
        const val APP_UPDATE_REQUEST_CODE = 3141
    }
}
