package me.tylerbwong.stack.data.auth

import android.net.Uri
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.api.service.AuthService
import me.tylerbwong.stack.api.service.UserService
import me.tylerbwong.stack.data.logging.Logger
import me.tylerbwong.stack.data.persistence.dao.AnswerDao
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.CommentDraftDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDraftDao
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import me.tylerbwong.stack.data.persistence.dao.SiteDao
import me.tylerbwong.stack.data.persistence.dao.UserDao
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("LongParameterList")
@Singleton
class AuthRepository @Inject constructor(
    private val questionDraftDao: QuestionDraftDao,
    private val answerDraftDao: AnswerDraftDao,
    private val commentDraftDao: CommentDraftDao,
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao,
    private val userDao: UserDao,
    private val searchDao: SearchDao,
    private val siteDao: SiteDao,
    private val userService: UserService,
    private val authService: AuthService,
    private val authStore: AuthStore,
    private val logger: Logger,
) {
    internal val isAuthenticated: Boolean
        get() = authStore.isAuthenticatedLiveData.value ?: false

    internal val isAuthenticatedLiveData: LiveData<Boolean>
        get() = authStore.isAuthenticatedLiveData

    suspend fun logIn(uri: Uri): LoginResult {
        authStore.setAccessToken(uri)
        return if (authStore.accessToken != null && getCurrentUser() != null) {
            authStore.updateAuthenticatedState(isAuthenticated = true)
            logger.logEvent(eventName = LOGGER_LOGIN_SUCCESS_EVENT_NAME)
            LoginResult.LoginSuccess
        } else {
            logger.logEvent(eventName = LOGGER_LOGIN_ERROR_EVENT_NAME)
            authStore.clear()
            LoginResult.LoginError
        }
    }

    suspend fun logOut(): LogOutResult {
        val accessToken = authStore.accessToken

        return try {
            if (!accessToken.isNullOrBlank()) {
                authService.logOut(accessToken = accessToken)
                authStore.clear()
                withContext(Dispatchers.IO) {
                    questionDraftDao.clearDrafts()
                    answerDraftDao.clearDrafts()
                    commentDraftDao.clearDrafts()
                    questionDao.clearQuestions()
                    answerDao.clearAnswers()
                    userDao.clearUsers()
                    searchDao.clearSearches()
                    siteDao.clearAssociatedSites()
                }
                logger.logEvent(eventName = LOGGER_LOGOUT_SUCCESS_EVENT_NAME)
                LogOutResult.LogOutSuccess
            } else {
                throw IllegalStateException("Could not log user out for null access token")
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            logger.logEvent(eventName = LOGGER_LOGOUT_ERROR_EVENT_NAME)
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

    companion object {
        private const val LOGGER_LOGIN_SUCCESS_EVENT_NAME = "login_success"
        private const val LOGGER_LOGIN_ERROR_EVENT_NAME = "login_error"
        private const val LOGGER_LOGOUT_SUCCESS_EVENT_NAME = "logout_success"
        private const val LOGGER_LOGOUT_ERROR_EVENT_NAME = "logout_error"
    }
}

sealed class LoginResult {
    data object LoginSuccess : LoginResult()
    data object LoginError : LoginResult()
}

sealed class LogOutResult {
    data object LogOutSuccess : LogOutResult()
    data object LogOutError : LogOutResult()
}
