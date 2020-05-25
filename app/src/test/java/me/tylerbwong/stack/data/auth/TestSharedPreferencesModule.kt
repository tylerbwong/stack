package me.tylerbwong.stack.data.auth

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.data.auth.di.AuthModule
import me.tylerbwong.stack.data.auth.di.SharedPreferencesModule

@Module
class TestSharedPreferencesModule : SharedPreferencesModule() {

    @Provides
    override fun provideSharedPreferences(
        context: Context
    ): SharedPreferences = context.getSharedPreferences(
        AuthModule.AUTH_PREFERENCES,
        Context.MODE_PRIVATE
    )
}
