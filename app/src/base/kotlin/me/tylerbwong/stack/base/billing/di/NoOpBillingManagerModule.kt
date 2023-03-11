package me.tylerbwong.stack.base.billing.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.tylerbwong.stack.data.billing.BillingManager
import me.tylerbwong.stack.data.billing.NoOpBillingManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NoOpBillingManagerModule {

    @[Provides Singleton]
    fun provideNoOpAppUpdater(): BillingManager = NoOpBillingManager()
}
