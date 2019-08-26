package me.tylerbwong.stack.data.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authUrlBuilder = chain.request().url.newBuilder()
        val accessToken = AuthProvider.accessToken

        if (!accessToken.isNullOrBlank()) {
            authUrlBuilder.addEncodedQueryParameter(AuthProvider.ACCESS_TOKEN, accessToken)
        }

        val authRequest = chain.request().newBuilder()
                .url(authUrlBuilder.build())
                .build()
        return chain.proceed(authRequest)
    }
}
