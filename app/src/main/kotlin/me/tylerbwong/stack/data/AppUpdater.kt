package me.tylerbwong.stack.data

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import dagger.hilt.android.scopes.ActivityScoped
import me.tylerbwong.stack.ui.MainActivity
import javax.inject.Inject

@ActivityScoped
class AppUpdater @Inject constructor(private val manager: AppUpdateManager) {

    fun checkForUpdate(activity: MainActivity) {
        manager.registerListener(activity)

        manager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                it.isUpdateTypeAllowed(FLEXIBLE)
            ) {
                manager.startUpdateFlowForResult(it, FLEXIBLE, activity, APP_UPDATE_REQUEST_CODE)
            }
        }
    }

    fun checkForPendingInstall(
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

    fun completeUpdate(): Task<Void> = manager.completeUpdate()

    fun unregisterListener(listener: InstallStateUpdatedListener) {
        manager.unregisterListener(listener)
    }

    companion object {
        const val APP_UPDATE_REQUEST_CODE = 3141
    }
}
