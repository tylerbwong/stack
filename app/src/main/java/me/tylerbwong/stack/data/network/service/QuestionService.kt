package me.tylerbwong.stack.data.network.service

import io.reactivex.Single
import me.tylerbwong.stack.data.model.QuestionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionService {
    @GET("questions")
    fun getQuestions(@Query("site") site: String = "stackoverflow"): Single<QuestionResponse>
}