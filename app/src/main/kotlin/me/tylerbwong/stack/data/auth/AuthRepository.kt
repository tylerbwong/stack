package me.tylerbwong.stack.data.auth

import android.net.Uri
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.api.service.AuthService
import me.tylerbwong.stack.api.service.UserService
import me.tylerbwong.stack.data.persistence.dao.AnswerDao
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDao
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import me.tylerbwong.stack.data.persistence.dao.UserDao
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val answerDraftDao: AnswerDraftDao,
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao,
    private val userDao: UserDao,
    private val searchDao: SearchDao,
    private val userService: UserService,
    private val authService: AuthService,
    private val authStore: AuthStore
) {
    internal val isAuthenticated: Boolean
        get() = authStore.isAuthenticatedLiveData.value ?: false

    internal val isAuthenticatedLiveData: LiveData<Boolean>
        get() = authStore.isAuthenticatedLiveData

    suspend fun logIn(uri: Uri): LoginResult {
        authStore.setAccessToken(uri)
        return if (authStore.accessToken != null && getCurrentUser() != null) {
            authStore.updateAuthenticatedState(isAuthenticated = true)
            LoginResult.LoginSuccess
        } else {
            authStore.clear()
            LoginResult.LoginError
        }
    }

    suspend fun logOut(): LogOutResult {
        val accessToken = authStore.accessToken

        return try {
            if (!accessToken.isNullOrBlank()) {
                withContext(Dispatchers.IO) {
                    answerDraftDao.clearDrafts()
                    questionDao.clearQuestions()
                    answerDao.clearAnswers()
                    userDao.clearUsers()
                    searchDao.clearSearches()
                }
                authService.logOut(accessToken = accessToken)
                authStore.clear()
                LogOutResult.LogOutSuccess
            } else {
                throw IllegalStateException("Could not log user out for null access token")
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            LogOutResult.LogOutError
        }
    }

    /**
     * Fetches the currently logged in user.
     *
     * @return A [User] instance if there is a valid accessToken, otherwise null
     */
    suspend fun getCurrentUser(): User? {
        return try {
            if (authStore.accessToken != null) {
                userService.getCurrentUser().items.firstOrNull()
            } else {
                null
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }
    }
}

sealed class LoginResult {
    object LoginSuccess : LoginResult()
    object LoginError : LoginResult()
}

sealed class LogOutResult {
    object LogOutSuccess : LogOutResult()
    object LogOutError : LogOutResult()
}
