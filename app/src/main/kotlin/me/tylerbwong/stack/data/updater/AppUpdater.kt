package me.tylerbwong.stack.data.updater

import me.tylerbwong.stack.ui.MainActivity

interface AppUpdater {
    fun checkForUpdate(activity: MainActivity)
    fun checkForPendingInstall(onDownloadFinished: () -> Unit, onDownloadFailed: () -> Unit)
    fun completeUpdate()
    fun unregisterListener()

    companion object {
        const val APP_UPDATE_REQUEST_CODE = 3141
    }
}
