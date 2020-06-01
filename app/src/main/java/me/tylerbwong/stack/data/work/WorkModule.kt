package me.tylerbwong.stack.data.work

import android.content.Context
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.data.repository.SiteRepository
import javax.inject.Singleton

@Module
class WorkModule {

    @Provides
    fun provideWorkerFactory(
        siteRepository: SiteRepository
    ) = SitesWorker.SitesWorkerFactory(siteRepository)

    @Provides
    fun provideWorkerConstraints(): Constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

    @Provides
    fun provideWorkRequest(
        constraints: Constraints
    ): WorkRequest = OneTimeWorkRequestBuilder<SitesWorker>()
        .setConstraints(constraints)
        .build()

    @Singleton
    @Provides
    fun provideWorkerManager(
        context: Context,
        sitesWorkerFactory: SitesWorker.SitesWorkerFactory
    ): WorkManager {
        WorkManager.initialize(
            context,
            Configuration.Builder()
                .setWorkerFactory(sitesWorkerFactory)
                .build()
        )
        return WorkManager.getInstance(context)
    }
}
