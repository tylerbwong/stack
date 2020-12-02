package me.tylerbwong.stack.data.work

import androidx.work.WorkManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduler @Inject constructor(
    private val workManager: WorkManager,
    private val workRequests: Set<@JvmSuppressWildcards Work<*>>
) {
    fun schedule() {
        // TODO Re-enable Work Manager when ready
        workManager.cancelAllWork()
    }

    private fun scheduleNewWork() {
        workRequests.forEach {
            when (it) {
                is Work.OneTime -> workManager
                    .enqueueUniqueWork(it.identifier, it.existingWorkPolicy, it.workRequest)
                is Work.Periodic -> workManager
                    .enqueueUniquePeriodicWork(it.identifier, it.existingWorkPolicy, it.workRequest)
            }
        }
    }
}
