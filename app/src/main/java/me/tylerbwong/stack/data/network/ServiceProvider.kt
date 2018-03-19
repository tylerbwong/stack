package me.tylerbwong.stack.data.network

import me.tylerbwong.stack.data.network.service.QuestionService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceProvider {

    private fun createRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("https://api.stackexchange.com/2.2/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(createOkHttpClient())
            .build()

    private fun createOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    fun getQuestionService(): QuestionService = createRetrofit().create(QuestionService::class.java)

}