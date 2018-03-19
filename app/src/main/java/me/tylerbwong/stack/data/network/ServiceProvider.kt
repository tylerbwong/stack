package me.tylerbwong.stack.data.network

import me.tylerbwong.stack.data.network.service.QuestionService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceProvider {

    val questionService: QuestionService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com/2.2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        questionService = retrofit.create(QuestionService::class.java)
    }

}