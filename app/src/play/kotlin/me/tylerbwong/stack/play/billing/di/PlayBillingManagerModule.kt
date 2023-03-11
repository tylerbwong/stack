package me.tylerbwong.stack.play.billing.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.tylerbwong.stack.data.billing.BillingManager
import me.tylerbwong.stack.play.billing.PlayBillingManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlayBillingManagerModule {

    @[Provides Singleton]
    fun providePlayBillingManager(
        @ApplicationContext context: Context,
    ): BillingManager = PlayBillingManager(context)
}
