package me.tylerbwong.stack.data.work

import androidx.work.WorkManager
import androidx.work.WorkRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduler @Inject constructor(
    private val workManager: WorkManager,
    private val workRequests: Set<@JvmSuppressWildcards WorkRequest>
) {
    fun schedule() {
        workRequests.forEach { workManager.enqueue(it) }
    }
}
