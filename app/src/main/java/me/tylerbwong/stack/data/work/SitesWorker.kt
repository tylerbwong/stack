package me.tylerbwong.stack.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.data.repository.SiteRepository
import timber.log.Timber

class SitesWorker(
    context: Context,
    params: WorkerParameters,
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

    class SitesWorkerFactory(private val siteRepository: SiteRepository) : WorkerFactory() {

        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker = SitesWorker(appContext, workerParameters, siteRepository)
    }
}
