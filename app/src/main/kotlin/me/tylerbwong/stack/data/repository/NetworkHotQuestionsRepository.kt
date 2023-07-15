package me.tylerbwong.stack.data.repository

import me.tylerbwong.stack.api.model.NetworkHotQuestion
import me.tylerbwong.stack.api.service.NetworkHotQuestionsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHotQuestionsRepository @Inject constructor(
    private val networkHotQuestionsService: NetworkHotQuestionsService,
) {
    suspend fun getHotNetworkQuestions(): List<NetworkHotQuestion> {
        return networkHotQuestionsService.getHotNetworkQuestions()
    }
}
