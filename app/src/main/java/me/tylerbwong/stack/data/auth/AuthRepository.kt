package me.tylerbwong.stack.data.auth

import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.StackService
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.persistence.dao.UserDao
import me.tylerbwong.stack.data.toUserEntity

class AuthRepository(
        private val userDao: UserDao = StackDatabase.getInstance().getUserDao(),
        private val service: StackService = ServiceProvider.stackService,
        private val authProvider: AuthProvider = AuthProvider
) {
    suspend fun logOut() {
        service.logOut(accessToken = authProvider.accessToken)
        authProvider.accessToken = null
    }

    suspend fun getCurrentUserNetwork(): User? {
        val users = service.getCurrentUser().items
        val userEntities = users.map { it.toUserEntity() }
        userDao.insert(userEntities)
        return users.firstOrNull()
    }
}
