package me.tylerbwong.stack.data.auth

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
    suspend fun logOut() {
        val accessToken = authProvider.accessToken

        if (accessToken != null) {
            service.logOut(accessToken = accessToken)
            authProvider.accessToken = null
        } else {
            Timber.e("Could not log user out for null access token")
        }
    }

    suspend fun getCurrentUser(): User? {
        val users = service.getCurrentUser().items
        val userEntities = users.map { it.toUserEntity() }
        userDao.insert(userEntities)
        return users.firstOrNull()
    }
}
