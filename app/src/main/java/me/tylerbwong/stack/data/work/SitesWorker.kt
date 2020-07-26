package me.tylerbwong.stack.data.work

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.data.repository.SiteRepository
import timber.log.Timber

class SitesWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val siteRepository: SiteRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {
        withContext(Dispatchers.IO) {
            siteRepository.fetchSitesIfNecessary()
            Result.success()
        }
    } catch (ex: Exception) {
        Timber.e(ex, "Failed to sync sites")
        Result.failure()
    }
}
