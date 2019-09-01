package me.tylerbwong.stack.data.auth

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authUrlBuilder = chain.request().url.newBuilder()
        val accessToken = AuthStore.accessToken

        // We only want to append the access_token as a query parameter if it exists
        // The API will return a error code if the query parameter is present with no valid value
        // Temporary hack for POST requests that have the access_token in the body instead of as a
        // query parameter
        if (!accessToken.isNullOrBlank() && !chain.request().isPost()) {
            authUrlBuilder.addEncodedQueryParameter(AuthStore.ACCESS_TOKEN, accessToken)
        }

        val authRequest = chain.request().newBuilder()
                .url(authUrlBuilder.build())
                .build()
        return chain.proceed(authRequest)
    }
}

private fun Request.isPost() = method.equals("post", ignoreCase = true)
