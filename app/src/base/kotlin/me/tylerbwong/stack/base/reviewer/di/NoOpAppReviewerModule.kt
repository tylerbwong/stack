package me.tylerbwong.stack.base.reviewer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import me.tylerbwong.stack.base.reviewer.NoOpAppReviewer
import me.tylerbwong.stack.data.reviewer.AppReviewer

@Module
@InstallIn(ActivityComponent::class)
class NoOpAppReviewerModule {

    @[Provides ActivityScoped]
    fun provideNoOpAppReviewer(): AppReviewer = NoOpAppReviewer()
}
