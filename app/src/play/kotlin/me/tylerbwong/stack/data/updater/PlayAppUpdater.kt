package me.tylerbwong.stack.data.updater

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import me.tylerbwong.stack.data.updater.AppUpdater.Companion.APP_UPDATE_REQUEST_CODE

class PlayAppUpdater(private val manager: AppUpdateManager) : AppUpdater {

    override fun checkForUpdate(activity: Activity) {
        if (activity !is InstallStateUpdatedListener) return
        manager.registerListener(activity)

        manager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                it.isUpdateTypeAllowed(FLEXIBLE)
            ) {
                manager.startUpdateFlowForResult(it, FLEXIBLE, activity, APP_UPDATE_REQUEST_CODE)
            }
        }
    }

    override fun checkForPendingInstall(
        onDownloadFinished: () -> Unit,
        onDownloadFailed: () -> Unit
    ) {
        manager.appUpdateInfo.addOnSuccessListener {
            when (it.installStatus()) {
                InstallStatus.DOWNLOADED -> onDownloadFinished()
                InstallStatus.FAILED -> onDownloadFailed()
            }
        }
    }

    override fun completeUpdate() {
        manager.completeUpdate()
    }

    override fun unregisterListener(activity: Activity) {
        if (activity !is InstallStateUpdatedListener) return
        manager.unregisterListener(activity)
    }
}
