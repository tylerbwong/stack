package me.tylerbwong.stack.data.updater.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import me.tylerbwong.stack.data.updater.AppUpdater
import me.tylerbwong.stack.data.updater.NoOpAppUpdater

@Module
@InstallIn(ActivityComponent::class)
class UpdaterModule {

    @[Provides ActivityScoped]
    fun provideNoOpAppUpdater(): AppUpdater = NoOpAppUpdater()
}
