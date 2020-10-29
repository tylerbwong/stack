package me.tylerbwong.stack.ui.di

import android.content.Context
import coil.imageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UiModule {

    @Provides
    fun provideImageLoader(@ApplicationContext context: Context) = context.imageLoader
}
