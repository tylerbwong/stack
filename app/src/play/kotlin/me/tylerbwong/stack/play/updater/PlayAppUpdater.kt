package me.tylerbwong.stack.play.updater

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import me.tylerbwong.stack.data.updater.AppUpdater
import me.tylerbwong.stack.ui.MainActivity

class PlayAppUpdater(private val manager: AppUpdateManager) : AppUpdater {

    private var listener: InstallStateUpdatedListener? = null

    override fun checkForUpdate(activity: MainActivity) {
        val listener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                activity.checkForPendingInstall()
            }
        }
        this.listener = listener
        manager.registerListener(listener)

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

    override fun unregisterListener() {
        listener?.let { manager.unregisterListener(it) }
    }

    companion object {
        const val APP_UPDATE_REQUEST_CODE = 3141
    }
}
