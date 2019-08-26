package me.tylerbwong.stack.data.auth

import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.StackService
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.persistence.dao.UserDao
import me.tylerbwong.stack.data.toUserEntity

class AuthRepository(
        private val userDao: UserDao = StackDatabase.getInstance().getUserDao(),
        private val service: StackService = ServiceProvider.stackService
) {
    suspend fun logOut(accessToken: String) = service.logOut(accessToken = accessToken)

    suspend fun getCurrentUserNetwork(): User? {
        val users = service.getCurrentUser().items
        val userEntities = users.map { it.toUserEntity() }
        userDao.insert(userEntities)
        return users.firstOrNull()
    }
}
