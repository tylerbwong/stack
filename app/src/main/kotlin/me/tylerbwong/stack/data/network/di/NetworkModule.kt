package me.tylerbwong.stack.data.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.auth.AuthInterceptor
import me.tylerbwong.stack.data.site.SiteInterceptor
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DebugInterceptor

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @[Provides IntoSet DebugInterceptor]
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @[Provides IntoSet DebugInterceptor]
    fun provideChuckerInterceptor(@ApplicationContext context: Context): Interceptor {
        val collector = ChuckerCollector(context, showNotification = false)
        return ChuckerInterceptor.Builder(context)
            .collector(collector)
            .build()
    }

    @[Provides Singleton]
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        siteInterceptor: SiteInterceptor,
        @DebugInterceptor debugInterceptors: Lazy<Set<@JvmSuppressWildcards Interceptor>>
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(siteInterceptor)

        if (BuildConfig.DEBUG) {
            debugInterceptors.get().forEach { okHttpClientBuilder.addInterceptor(it) }
        }

        return okHttpClientBuilder.build()
    }

    @[Provides Singleton]
    fun provideCallFactory(
        okHttpClient: Lazy<OkHttpClient>
    ) = Call.Factory { request -> okHttpClient.get().newCall(request) }
}
