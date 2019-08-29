package me.tylerbwong.stack.data

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import me.tylerbwong.stack.ui.MainActivity

class AppUpdater(private val manager: AppUpdateManager) {

    fun checkForUpdate(activity: MainActivity) {
        manager.registerListener(activity)

        manager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    it.isUpdateTypeAllowed(FLEXIBLE)) {
                manager.startUpdateFlowForResult(it, FLEXIBLE, activity, APP_UPDATE_REQUEST_CODE)
            }
        }
    }

    fun checkForPendingInstall(onDownloadFinished: (AppUpdateManager) -> Unit) {
        manager.appUpdateInfo.addOnSuccessListener {
            if (it.installStatus() == InstallStatus.DOWNLOADED) {
                onDownloadFinished(manager)
            }
        }
    }

    fun unregisterListener(listener: InstallStateUpdatedListener) {
        manager.unregisterListener(listener)
    }

    companion object {
        const val APP_UPDATE_REQUEST_CODE = 3141
    }
}
