package me.tylerbwong.stack.data.repository

import me.tylerbwong.stack.api.model.NetworkHotQuestion
import me.tylerbwong.stack.api.service.NetworkService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val networkService: NetworkService,
) {
    suspend fun getHotNetworkQuestions(): List<NetworkHotQuestion> {
        return networkService.getHotNetworkQuestions()
    }
}
