package me.tylerbwong.stack.play.logging.di

import android.content.SharedPreferences
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.di.Initializer
import me.tylerbwong.stack.data.di.StackSharedPreferences
import me.tylerbwong.stack.data.logging.CrashReporting
import me.tylerbwong.stack.play.logging.CrashlyticsReporting
import me.tylerbwong.stack.play.logging.CrashlyticsTree
import timber.log.Timber
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CrashlyticsModule {

    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @[Provides Singleton]
    fun provideCrashReporting(
        firebaseCrashlytics: FirebaseCrashlytics,
        @StackSharedPreferences preferences: SharedPreferences,
    ): CrashReporting = CrashlyticsReporting(firebaseCrashlytics, preferences)

    @[Provides Initializer IntoSet]
    fun provideCrashlyticsInitializer(
        crashReporting: CrashReporting,
        crashlyticsTree: Provider<CrashlyticsTree>
    ): () -> Unit = {
        crashReporting.initialize()
        if (!BuildConfig.DEBUG && crashReporting.isCrashReportingEnabled) {
            Timber.plant(crashlyticsTree.get())
        }
    }
}
