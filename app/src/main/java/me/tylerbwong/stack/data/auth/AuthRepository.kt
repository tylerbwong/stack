package me.tylerbwong.stack.data.auth

import me.tylerbwong.stack.data.auth.LogOutResult.LogOutError
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutSuccess
import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.StackService
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.persistence.dao.UserDao
import me.tylerbwong.stack.data.toUserEntity
import timber.log.Timber

class AuthRepository(
        private val userDao: UserDao = StackDatabase.getInstance().getUserDao(),
        private val service: StackService = ServiceProvider.stackService,
        private val authProvider: AuthProvider = AuthProvider
) {
    suspend fun logOut(): LogOutResult {
        val accessToken = authProvider.accessToken

        return try {
            if (!accessToken.isNullOrBlank()) {
                service.logOut(accessToken = accessToken)
                authProvider.accessToken = null
                LogOutSuccess
            } else {
                throw IllegalStateException("Could not log user out for null access token")
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            LogOutError
        }
    }

    /**
     * Fetches the currently logged in user.
     *
     * @return A [User] instance if there is a valid accessToken, otherwise null
     */
    suspend fun getCurrentUser(): User? {
        val isAuthenticated = !authProvider.accessToken.isNullOrBlank()

        return if (isAuthenticated) {
            val users = service.getCurrentUser().items
            val userEntities = users.map { it.toUserEntity() }
            userDao.insert(userEntities)
            users.firstOrNull()
        } else {
            null
        }
    }
}

sealed class LogOutResult {
    object LogOutSuccess : LogOutResult()
    object LogOutError : LogOutResult()
}
