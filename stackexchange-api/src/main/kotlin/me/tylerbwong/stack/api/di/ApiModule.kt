package me.tylerbwong.stack.api.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import me.tylerbwong.stack.api.BuildConfig
import me.tylerbwong.stack.api.UnitConverterFactory
import me.tylerbwong.stack.api.service.AuthService
import me.tylerbwong.stack.api.service.CommentService
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.api.service.SearchService
import me.tylerbwong.stack.api.service.SiteService
import me.tylerbwong.stack.api.service.TagService
import me.tylerbwong.stack.api.service.UserService
import okhttp3.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @[Provides BaseUrl]
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @[Provides IntoSet]
    fun provideUnitConverterFactory(): Converter.Factory = UnitConverterFactory

    @[Provides IntoSet]
    fun provideMoshiConverterFactory(
        moshi: Moshi
    ): Converter.Factory = MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        callFactory: Call.Factory,
        converterFactories: Set<@JvmSuppressWildcards Converter.Factory>
    ): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .callFactory(callFactory)
        converterFactories.forEach { builder.addConverterFactory(it) }
        return builder.build()
    }

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
        private const val BASE_URL = "https://api.stackexchange.com/${BuildConfig.API_VERSION}/"
    }
}
