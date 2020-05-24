package me.tylerbwong.stack.data.auth.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides

@Module
open class SharedPreferencesModule {

    @Provides
    open fun provideSharedPreferences(
        context: Context
    ): SharedPreferences = EncryptedSharedPreferences.create(
        AuthModule.AUTH_PREFERENCES,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
