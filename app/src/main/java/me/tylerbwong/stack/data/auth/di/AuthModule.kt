package me.tylerbwong.stack.data.auth.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.data.auth.AuthInterceptor
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.network.service.AuthService
import me.tylerbwong.stack.data.network.service.UserService
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import javax.inject.Singleton

@Module
class AuthModule {

    @Provides
    fun provideSharedPreferences(
        context: Context
    ) = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideAuthStore(preferences: SharedPreferences) = AuthStore(preferences)

    @Singleton
    @Provides
    fun provideAuthRepository(
        answerDraftDao: AnswerDraftDao,
        searchDao: SearchDao,
        userService: UserService,
        authService: AuthService,
        authStore: AuthStore
    ) = AuthRepository(answerDraftDao, searchDao, userService, authService, authStore)

    @Provides
    fun provideAuthInterceptor(
        baseUrl: String,
        authStore: AuthStore
    ) = AuthInterceptor(baseUrl, authStore)

    companion object {
        private const val AUTH_PREFERENCES = "auth_preferences"
    }
}
