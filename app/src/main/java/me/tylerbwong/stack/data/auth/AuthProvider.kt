package me.tylerbwong.stack.data.auth

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.tylerbwong.stack.data.model.Scope
import me.tylerbwong.stack.ui.ApplicationWrapper
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.HttpException
import timber.log.Timber

object AuthProvider {
    const val ACCESS_TOKEN = "access_token"
    private const val ACCOUNT_ID = "account_id"
    private const val AUTH_PREFERENCES = "auth_preferences"
    private const val AUTH_BASE = "https://stackoverflow.com/oauth/dialog"
    private const val AUTH_REDIRECT = "stack://tylerbwong.me/auth/redirect"
    private const val CLIENT_ID = "12074"

    private val repository = AuthRepository()

    private val preferences: SharedPreferences
        get() = ApplicationWrapper.context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)

    val authUrl: String
        get() {
            val httpUrl = AUTH_BASE.toHttpUrlOrNull() ?: return ""

            return httpUrl.newBuilder()
                    .addEncodedQueryParameter("client_id", CLIENT_ID)
                    .addEncodedQueryParameter("redirect_uri", AUTH_REDIRECT)
                    .addEncodedQueryParameter("scope", Scope.all.joinToString(","))
                    .build()
                    .toString()
        }

    var accessToken: String?
        get() = preferences.getString(ACCESS_TOKEN, "")
        set(value) {
            preferences.edit().putString(ACCESS_TOKEN, value).apply()
            mutableIsAuthenticatedLiveData.postValue(!value.isNullOrBlank())
        }

    var accountId: Int?
        get() = preferences.getInt(ACCOUNT_ID, -1)
        set(value) {
            preferences.edit().putInt(ACCOUNT_ID, value ?: -1).apply()
            mutableIsAuthenticatedLiveData.postValue(!accessToken.isNullOrBlank())
        }

    val isAuthenticatedLiveData: LiveData<Boolean>
        get() = mutableIsAuthenticatedLiveData
    private val mutableIsAuthenticatedLiveData = MutableLiveData(!accessToken.isNullOrBlank())

    fun init() {
        GlobalScope.launch {
            val user = try {
                repository.getCurrentUserNetwork()
            } catch (ex: HttpException) {
                Timber.i("User not available, not authed.")
                null
            }

            if (user == null) {
                accountId = null
                accessToken = null
            } else {
                accountId = user.accountId
            }
        }
    }

    suspend fun logOut() {
        accessToken?.let {
            repository.logOut(it)
        }

        accountId = null
        accessToken = null
    }

    fun setAccessToken(uri: Uri) {
        accessToken = Uri.parse("$AUTH_REDIRECT?${uri.encodedFragment}").getQueryParameter(ACCESS_TOKEN)
    }
}
