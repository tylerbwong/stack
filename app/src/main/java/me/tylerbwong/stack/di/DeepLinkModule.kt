package me.tylerbwong.stack.di

import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.data.DeepLinker
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.ui.ApplicationWrapper

@Module
class DeepLinkModule {

    @Provides
    fun provideContext() = ApplicationWrapper.context

    @Provides
    fun provideDeepLinker(authStore: AuthStore) = DeepLinker(authStore)
}
