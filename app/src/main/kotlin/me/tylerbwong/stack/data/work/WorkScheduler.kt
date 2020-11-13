package me.tylerbwong.stack.data.work

import androidx.lifecycle.LifecycleOwner
import androidx.work.WorkManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduler @Inject constructor(
    private val workManager: WorkManager,
    private val workRequests: Set<@JvmSuppressWildcards Work<*>>
) {
    /**
     * We only want uniquely identified work to run. If there is no pending work that is identified
     * by [BookmarksWorker.IDENTIFIER], cancel all pending work before scheduling new work.
     *
     * @param lifecycleOwner The host [LifecycleOwner] that will be in charge of observing work info
     * requests.
     */
    fun schedule(lifecycleOwner: LifecycleOwner) {
        // TODO Delete this once enough people are on the new version
        val validWorkRequest = workRequests.find {
            it.identifier == BookmarksWorker.IDENTIFIER
        } ?: return
        val workInfoLiveData = workManager.getWorkInfosForUniqueWorkLiveData(
            validWorkRequest.identifier
        )
        workInfoLiveData.observe(lifecycleOwner) {
            if (it.isEmpty()) {
                workManager.cancelAllWork()
            }
            scheduleNewWork()
            workInfoLiveData.removeObservers(lifecycleOwner) // Only want to listen once
        }
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
