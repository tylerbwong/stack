package me.tylerbwong.stack.data.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WorkModule {

    @Provides
    fun provideWorkerConstraints(): Constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

    @[Provides IntoSet]
    fun provideSitesWorkRequest(
        constraints: Constraints
    ): Work<*> = Work.OneTime(
        identifier = SitesWorker.IDENTIFIER,
        workRequest = OneTimeWorkRequestBuilder<SitesWorker>()
            .addTag(SitesWorker.IDENTIFIER)
            .setConstraints(constraints)
            .build(),
        existingWorkPolicy = ExistingWorkPolicy.REPLACE
    )

    // TODO Make interval configurable
    @[Provides IntoSet]
    fun provideBookmarksWorkRequest(
        constraints: Constraints
    ): Work<*> = Work.Periodic(
        identifier = BookmarksWorker.IDENTIFIER,
        workRequest = PeriodicWorkRequestBuilder<BookmarksWorker>(8, TimeUnit.HOURS)
            .addTag(BookmarksWorker.IDENTIFIER)
            .setConstraints(constraints)
            .build(),
        existingWorkPolicy = ExistingPeriodicWorkPolicy.KEEP
    )

    @[Provides Singleton]
    fun provideWorkerManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)
}
