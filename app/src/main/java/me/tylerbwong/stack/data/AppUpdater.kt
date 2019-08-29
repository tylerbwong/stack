package me.tylerbwong.stack.data

import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

object AppUpdater {

    const val APP_UPDATE_REQUEST_CODE = 3141

    fun checkForUpdate(activity: AppCompatActivity) {
        val manager = AppUpdateManagerFactory.create(activity)

        manager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    it.isUpdateTypeAllowed(FLEXIBLE)) {
                manager.startUpdateFlowForResult(it, FLEXIBLE, activity, APP_UPDATE_REQUEST_CODE)
            }
        }
    }

    fun checkForPendingInstall(
            activity: AppCompatActivity,
            onDownloadFinished: (AppUpdateManager) -> Unit
    ) {
        val manager = AppUpdateManagerFactory.create(activity)

        manager.appUpdateInfo.addOnSuccessListener {
            if (it.installStatus() == InstallStatus.DOWNLOADED) {
                onDownloadFinished(manager)
            }
        }
    }
}
