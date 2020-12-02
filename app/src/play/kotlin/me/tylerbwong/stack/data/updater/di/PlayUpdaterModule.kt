package me.tylerbwong.stack.data.updater.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import me.tylerbwong.stack.data.PlayAppUpdater
import me.tylerbwong.stack.data.updater.AppUpdater

@Module
@InstallIn(ActivityComponent::class)
class PlayUpdaterModule {

    @[Provides ActivityScoped]
    fun provideAppUpdateManager(
        @ActivityContext context: Context
    ): AppUpdateManager = AppUpdateManagerFactory.create(context)

    @Provides
    fun providePlayAppUpdater(manager: AppUpdateManager): AppUpdater = PlayAppUpdater(manager)
}
