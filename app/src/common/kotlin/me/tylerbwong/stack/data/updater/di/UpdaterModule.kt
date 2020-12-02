package me.tylerbwong.stack.data.updater.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import me.tylerbwong.stack.data.updater.AppUpdater
import me.tylerbwong.stack.data.updater.NoOpAppUpdater

@Module
@InstallIn(ActivityComponent::class)
class UpdaterModule {

    @Provides
    fun provideNoOpAppUpdater(): AppUpdater = NoOpAppUpdater()
}
