package me.tylerbwong.stack.data.work

import androidx.work.WorkManager
import androidx.work.WorkRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduler @Inject constructor(
    private val workManager: WorkManager,
    private val siteWorkRequest: WorkRequest
) {

    fun schedule() {
        workManager.enqueue(siteWorkRequest)
    }
}
