package me.tylerbwong.stack.data.logging.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.tylerbwong.stack.data.logging.CrashlyticsTree
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CrashlyticsLoggerModule {

    @[Provides Singleton]
    fun provideTimberTree(): Timber.Tree {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        return CrashlyticsTree()
    }
}
