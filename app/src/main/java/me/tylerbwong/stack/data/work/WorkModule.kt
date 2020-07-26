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
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WorkModule {

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
