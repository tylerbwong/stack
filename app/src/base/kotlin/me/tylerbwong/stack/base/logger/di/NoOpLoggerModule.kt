package me.tylerbwong.stack.base.logger.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.tylerbwong.stack.data.logging.CrashReporting
import me.tylerbwong.stack.data.logging.Logger
import me.tylerbwong.stack.data.logging.NoOpCrashReporting
import me.tylerbwong.stack.data.logging.NoOpLogger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NoOpLoggerModule {

    @[Provides Singleton]
    fun provideNoOpLogger(): Logger = NoOpLogger()

    @[Provides Singleton]
    fun provideNoOpCrashReporting(): CrashReporting = NoOpCrashReporting()
}
