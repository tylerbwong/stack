package me.tylerbwong.stack.data.network

import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.auth.AuthInterceptor
import me.tylerbwong.stack.data.network.service.StackService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceProvider {

    private val okHttpClient by lazy {
        val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(BASE_URL))

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        okHttpClientBuilder.build()
    }

    val stackService: StackService by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StackService::class.java)
    }

    private const val BASE_URL = "https://api.stackexchange.com/2.2/"
    internal const val DEFAULT_KEY = ")vdLbYccKv*tSRXeypGGeA(("
}
