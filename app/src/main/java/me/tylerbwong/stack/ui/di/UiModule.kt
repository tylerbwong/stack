package me.tylerbwong.stack.ui.di

import android.content.Context
import coil.Coil
import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.ui.ApplicationWrapper

@Module
class UiModule {

    @Provides
    fun provideApplicationContext() = ApplicationWrapper.context

    @Provides
    fun provideImageLoader(context: Context) = Coil.imageLoader(context)
}
