package me.tylerbwong.stack.ui.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class MainModule {

    @[Provides ActivityScoped]
    fun provideAppUpdateManager(
        @ActivityContext context: Context
    ): AppUpdateManager = AppUpdateManagerFactory.create(context)
}
