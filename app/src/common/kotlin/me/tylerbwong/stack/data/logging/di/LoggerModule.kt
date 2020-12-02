package me.tylerbwong.stack.data.logging.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.tylerbwong.stack.data.logging.NoOpTree
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LoggerModule {

    @[Provides Singleton]
    fun provideNoOpTree(): Timber.Tree = NoOpTree()
}
