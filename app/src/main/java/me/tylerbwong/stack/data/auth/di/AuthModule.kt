package me.tylerbwong.stack.data.auth.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
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
    ) = EncryptedSharedPreferences.create(
        AUTH_PREFERENCES,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

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
