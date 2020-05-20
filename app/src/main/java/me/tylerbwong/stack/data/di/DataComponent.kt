package me.tylerbwong.stack.data.di

import com.squareup.moshi.Moshi
import dagger.Component
import me.tylerbwong.stack.data.auth.AuthInterceptor
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.network.UnitConverterFactory
import me.tylerbwong.stack.data.network.service.AuthService
import me.tylerbwong.stack.data.network.service.CommentService
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.data.network.service.UserService
import me.tylerbwong.stack.data.auth.di.AuthModule
import me.tylerbwong.stack.data.network.di.NetworkModule
import me.tylerbwong.stack.data.persistence.di.PersistenceModule
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDao
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import me.tylerbwong.stack.data.persistence.dao.UserDao
import me.tylerbwong.stack.di.StackModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AuthModule::class,
        NetworkModule::class,
        PersistenceModule::class,
        StackModule::class
    ]
)
interface DataComponent {
    fun authStore(): AuthStore
    fun authRepository(): AuthRepository
    fun authInterceptor(): AuthInterceptor

    fun baseUrl(): String
    fun moshi(): Moshi
    fun unitConverterFactory(): UnitConverterFactory
    fun moshiConverterFactor(): MoshiConverterFactory
    fun okHttpClient(): OkHttpClient
    fun retrofit(): Retrofit
    fun authService(): AuthService
    fun userService(): UserService
    fun searchService(): SearchService
    fun questionService(): QuestionService
    fun commentService(): CommentService
    fun tagService(): TagService

    fun questionDao(): QuestionDao
    fun userDao(): UserDao
    fun answerDraftDao(): AnswerDraftDao
    fun searchDao(): SearchDao
}
