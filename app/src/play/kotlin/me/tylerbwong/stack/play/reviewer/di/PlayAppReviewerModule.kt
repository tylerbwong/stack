package me.tylerbwong.stack.play.reviewer.di

import android.content.Context
import android.content.SharedPreferences
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import me.tylerbwong.stack.data.di.StackSharedPreferences
import me.tylerbwong.stack.data.reviewer.AppReviewer
import me.tylerbwong.stack.play.reviewer.PlayAppReviewer

@Module
@InstallIn(ActivityComponent::class)
class PlayAppReviewerModule {

    @[Provides ActivityScoped]
    fun provideReviewManager(
        @ActivityContext context: Context
    ): ReviewManager = ReviewManagerFactory.create(context)

    @[Provides ActivityScoped]
    fun providePlayAppReviewer(
        manager: ReviewManager,
        @StackSharedPreferences preferences: SharedPreferences,
    ): AppReviewer = PlayAppReviewer(manager, preferences)
}
