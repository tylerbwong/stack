package me.tylerbwong.stack.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import me.tylerbwong.stack.BuildConfig
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
class LoggingModule {

    @[Provides Initializer IntoSet]
    fun provideLoggingInitializer(): () -> Unit = {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
