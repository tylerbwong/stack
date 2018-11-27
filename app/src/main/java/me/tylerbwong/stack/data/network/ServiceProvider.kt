package me.tylerbwong.stack.data.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.network.service.QuestionService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.noties.markwon.il.AsyncDrawableLoader
import ru.noties.markwon.il.NetworkSchemeHandler

object ServiceProvider {

    private val okHttpClient by lazy {
        val okHttpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            okHttpClientBuilder.addInterceptor(StethoInterceptor())
        }

        okHttpClientBuilder.build()
    }

    val questionService: QuestionService by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com/2.2/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        retrofit.create(QuestionService::class.java)
    }

    val asyncDrawableLoader: AsyncDrawableLoader by lazy {
        AsyncDrawableLoader.builder()
                .addSchemeHandler(NetworkSchemeHandler.create(okHttpClient))
                .build()
    }

    internal const val DEFAULT_KEY = ")vdLbYccKv*tSRXeypGGeA(("
}
