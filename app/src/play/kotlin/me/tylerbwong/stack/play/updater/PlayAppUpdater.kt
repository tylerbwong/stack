package me.tylerbwong.stack.play.updater

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import me.tylerbwong.stack.data.updater.AppUpdater

class PlayAppUpdater(private val manager: AppUpdateManager) : AppUpdater {

    private var listener: InstallStateUpdatedListener? = null

    override fun checkForUpdate(
        checkForPendingInstall: () -> Unit,
        activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
    ) {
        val listener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                checkForPendingInstall()
            }
        }
        this.listener = listener
        manager.registerListener(listener)

        manager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                it.isUpdateTypeAllowed(FLEXIBLE)
            ) {
                manager.startUpdateFlowForResult(
                    it,
                    activityResultLauncher,
                    AppUpdateOptions.defaultOptions(FLEXIBLE),
                )
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
}
