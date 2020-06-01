package me.tylerbwong.stack.data.auth.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.data.SiteStore
import javax.inject.Named

@Module
open class SharedPreferencesModule {

    @Provides
    @Named("siteSharedPreferences")
    fun provideSiteSharedPreferences(
        context: Context
    ): SharedPreferences = context.getSharedPreferences(
        SiteStore.SITE_PREFERENCES,
        Context.MODE_PRIVATE
    )

    @Provides
    @Named("authSharedPreferences")
    open fun provideAuthSharedPreferences(
        context: Context
    ): SharedPreferences = EncryptedSharedPreferences.create(
        AuthModule.AUTH_PREFERENCES,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
