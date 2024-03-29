package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.model.NetworkHotQuestion
import retrofit2.http.GET

interface NetworkHotQuestionsService {
    @GET("hot-questions-json")
    suspend fun getHotNetworkQuestions(): List<NetworkHotQuestion>
}
