package me.tylerbwong.stack.data.network.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.auth.AuthInterceptor
import me.tylerbwong.stack.data.network.SiteInterceptor
import me.tylerbwong.stack.data.network.UnitConverterFactory
import me.tylerbwong.stack.data.network.service.AuthService
import me.tylerbwong.stack.data.network.service.CommentService
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.SiteService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.data.network.service.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun provideUnitConverterFactory() = UnitConverterFactory

    @Provides
    fun provideMoshiConverterFactory(
        moshi: Moshi
    ): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideSiteInterceptor(
        baseUrl: String,
        siteStore: SiteStore
    ) = SiteInterceptor(baseUrl, siteStore)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        siteInterceptor: SiteInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(siteInterceptor)

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
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(unitConverterFactory)
        .addConverterFactory(moshiConverterFactory)
        .build()

    @Singleton
    @Provides
    fun provideAuthService(
        retrofit: Retrofit
    ): AuthService = retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideUserService(
        retrofit: Retrofit
    ): UserService = retrofit.create(UserService::class.java)

    @Singleton
    @Provides
    fun provideSearchService(
        retrofit: Retrofit
    ): SearchService = retrofit.create(SearchService::class.java)

    @Singleton
    @Provides
    fun provideQuestionService(
        retrofit: Retrofit
    ): QuestionService = retrofit.create(QuestionService::class.java)

    @Singleton
    @Provides
    fun provideCommentService(
        retrofit: Retrofit
    ): CommentService = retrofit.create(CommentService::class.java)

    @Singleton
    @Provides
    fun provideTagService(
        retrofit: Retrofit
    ): TagService = retrofit.create(TagService::class.java)

    @Singleton
    @Provides
    fun provideSiteService(
        retrofit: Retrofit
    ): SiteService = retrofit.create(SiteService::class.java)

    companion object {
        private const val BASE_URL = "https://api.stackexchange.com/2.2/"
    }
}
