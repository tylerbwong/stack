package me.tylerbwong.stack.play.logging.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.di.Initializer
import me.tylerbwong.stack.data.di.StackSharedPreferences
import me.tylerbwong.stack.data.logging.Logger
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.play.logging.FirebaseLogger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @[Provides Initializer IntoSet]
    fun provideFirebaseInitializer(@ApplicationContext context: Context): () -> Unit = {
        FirebaseApp.initializeApp(context)
    }

    @Provides
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    @[Provides Singleton]
    fun provideFirebaseLogger(
        analytics: FirebaseAnalytics,
        authStore: AuthStore,
        siteStore: SiteStore,
        @StackSharedPreferences preferences: SharedPreferences,
    ): Logger = FirebaseLogger(analytics, authStore, siteStore, preferences)
}
