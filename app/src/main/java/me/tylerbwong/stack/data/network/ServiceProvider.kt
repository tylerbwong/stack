package me.tylerbwong.stack.data.network

import io.reactivex.schedulers.Schedulers
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.network.service.QuestionService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.noties.markwon.il.AsyncDrawableLoader

object ServiceProvider {

    val questionService: QuestionService

    val asyncDrawableLoader: AsyncDrawableLoader

    const val DEFAULT_KEY = ")vdLbYccKv*tSRXeypGGeA(("

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        val okHttpClient = okHttpClientBuilder.build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com/2.2/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
                )
                .build()
        questionService = retrofit.create(QuestionService::class.java)

        asyncDrawableLoader = AsyncDrawableLoader.builder()
                .client(okHttpClient)
                .build()
    }
}
