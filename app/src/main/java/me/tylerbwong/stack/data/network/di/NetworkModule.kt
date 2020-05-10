package me.tylerbwong.stack.data.network.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.auth.AuthInterceptor
import me.tylerbwong.stack.data.network.UnitConverterFactory
import me.tylerbwong.stack.data.network.service.AuthService
import me.tylerbwong.stack.data.network.service.CommentService
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.data.network.service.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideMoshi() = Moshi.Builder().build()

    @Provides
    fun provideUnitConverterFactory() = UnitConverterFactory

    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi) = MoshiConverterFactory.create(moshi)

    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        unitConverterFactory: UnitConverterFactory,
        moshiConverterFactory: MoshiConverterFactory
    ) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(unitConverterFactory)
        .addConverterFactory(moshiConverterFactory)
        .build()

    @Singleton
    @Provides
    fun provideAuthService(retrofit: Retrofit) = retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit) = retrofit.create(UserService::class.java)

    @Singleton
    @Provides
    fun provideSearchService(retrofit: Retrofit) = retrofit.create(SearchService::class.java)

    @Singleton
    @Provides
    fun provideQuestionService(retrofit: Retrofit) = retrofit.create(QuestionService::class.java)

    @Singleton
    @Provides
    fun provideCommentService(retrofit: Retrofit) = retrofit.create(CommentService::class.java)

    @Singleton
    @Provides
    fun provideTagService(retrofit: Retrofit) = retrofit.create(TagService::class.java)

    companion object {
        private const val BASE_URL = "https://api.stackexchange.com/2.2/"
    }
}
