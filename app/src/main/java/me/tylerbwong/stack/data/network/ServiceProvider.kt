package me.tylerbwong.stack.data.network

import com.squareup.moshi.Moshi
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.auth.AuthInterceptor
import me.tylerbwong.stack.data.network.service.AuthService
import me.tylerbwong.stack.data.network.service.CommentService
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.data.network.service.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceProvider {

    internal val moshi by lazy { Moshi.Builder().build() }

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

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(UnitConverterFactory)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val questionService: QuestionService by lazy {
        retrofit.create(QuestionService::class.java)
    }

    val commentService: CommentService by lazy {
        retrofit.create(CommentService::class.java)
    }

    val tagService: TagService by lazy {
        retrofit.create(TagService::class.java)
    }

    private const val BASE_URL = "https://api.stackexchange.com/2.2/"
    internal const val DEFAULT_KEY = ")vdLbYccKv*tSRXeypGGeA(("
}
