package me.tylerbwong.stack.data.auth

import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.di.AuthSharedPreferences
import me.tylerbwong.stack.data.model.Scope
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStore @Inject constructor(
    @AuthSharedPreferences private val preferences: SharedPreferences
) {
    var accessToken: String?
        @Synchronized
        get() = preferences.getString(ACCESS_TOKEN, null)
        private set(value) {
            preferences.edit().putString(ACCESS_TOKEN, value).commit()
        }

    private val mutableIsAuthenticatedLiveData = MutableLiveData(!accessToken.isNullOrBlank())

    val isAuthenticatedLiveData: LiveData<Boolean> = mutableIsAuthenticatedLiveData

    fun setAccessToken(uri: Uri) {
        accessToken = Uri.parse("$AUTH_REDIRECT?${uri.encodedFragment}")
            .getQueryParameter(ACCESS_TOKEN)
    }

    fun clear() {
        accessToken = null
        preferences.edit().clear().apply()
        updateAuthenticatedState(isAuthenticated = false)
    }

    internal fun updateAuthenticatedState(isAuthenticated: Boolean) {
        mutableIsAuthenticatedLiveData.value = isAuthenticated
    }

    companion object {
        const val ACCESS_TOKEN = "access_token"
        internal const val AUTH_PREFERENCES = "auth_preferences"
        private const val AUTH_BASE = "https://stackoverflow.com/oauth/dialog"
        private const val AUTH_REDIRECT = "stack://tylerbwong.me/auth/redirect"

        val authUrl: String = run {
            val httpUrl = AUTH_BASE.toHttpUrlOrNull() ?: return@run ""

            httpUrl.newBuilder()
                .addEncodedQueryParameter("client_id", BuildConfig.CLIENT_ID)
                .addEncodedQueryParameter("redirect_uri", AUTH_REDIRECT)
                .addEncodedQueryParameter("scope", Scope.all.joinToString(","))
                .build()
                .toString()
        }
    }
}
