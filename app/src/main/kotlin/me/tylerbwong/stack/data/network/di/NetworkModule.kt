package me.tylerbwong.stack.data.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.data.auth.AuthInterceptor
import me.tylerbwong.stack.data.network.SiteInterceptor
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideChuckerInterceptor(context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(context, showNotification = false)
        return ChuckerInterceptor(context, collector)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        siteInterceptor: SiteInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(siteInterceptor)

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chuckerInterceptor)
        }

        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideCallFactory(
        okHttpClient: Lazy<OkHttpClient>
    ) = Call.Factory { request -> okHttpClient.get().newCall(request) }
}
