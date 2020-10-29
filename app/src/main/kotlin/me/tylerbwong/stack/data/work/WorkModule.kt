package me.tylerbwong.stack.data.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
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
    ): WorkRequest = OneTimeWorkRequestBuilder<SitesWorker>()
        .setConstraints(constraints)
        .build()

    // TODO Make interval configurable
    @[Provides IntoSet]
    fun provideBookmarksWorkRequest(
        constraints: Constraints
    ): WorkRequest = PeriodicWorkRequestBuilder<BookmarksWorker>(8, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    @[Provides Singleton]
    fun provideWorkerManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)
}
