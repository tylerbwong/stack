package me.tylerbwong.stack.data.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
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

    @[Provides IntoSet]
    fun provideBookmarksWorkRequest(
        constraints: Constraints
    ): WorkRequest = OneTimeWorkRequestBuilder<BookmarksWorker>()
        .setConstraints(constraints)
        .build()

    @Singleton
    @Provides
    fun provideWorkerManager(context: Context): WorkManager = WorkManager.getInstance(context)
}
