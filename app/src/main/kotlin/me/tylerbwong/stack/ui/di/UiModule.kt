package me.tylerbwong.stack.ui.di

import android.content.Context
import coil.Coil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.tylerbwong.stack.ui.ApplicationWrapper

@Module
@InstallIn(SingletonComponent::class)
class UiModule {

    @Provides
    fun provideApplicationContext() = ApplicationWrapper.context

    @Provides
    fun provideImageLoader(context: Context) = Coil.imageLoader(context)
}
