package me.tylerbwong.stack.base.updater.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import me.tylerbwong.stack.base.updater.NoOpAppUpdater
import me.tylerbwong.stack.data.updater.AppUpdater

@Module
@InstallIn(ActivityComponent::class)
class NoOpAppUpdaterModule {

    @[Provides ActivityScoped]
    fun provideNoOpAppUpdater(): AppUpdater = NoOpAppUpdater()
}
