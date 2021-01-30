package me.tylerbwong.stack.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.data.repository.SiteRepository
import timber.log.Timber

@HiltWorker
class SitesWorker @AssistedInject constructor(
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

    companion object {
        internal const val IDENTIFIER = "sites-worker"
    }
}
