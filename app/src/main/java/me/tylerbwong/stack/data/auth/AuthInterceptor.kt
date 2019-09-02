package me.tylerbwong.stack.data.auth

import me.tylerbwong.stack.data.auth.utils.addField
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(
        private val baseUrl: String,
        private val authStore: AuthStore = AuthStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // We do not want to add authentication to any request not going to api.stackexchange.com
        if (!request.isStack()) {
            return chain.proceed(request)
        }

        val authUrlBuilder = request.url.newBuilder()
        val accessToken = authStore.accessToken

        if (!accessToken.isNullOrBlank() && !request.isPost()) {
            authUrlBuilder.addEncodedQueryParameter(AuthStore.ACCESS_TOKEN, accessToken)
        }

        val authenticatedRequestBuilder = request.newBuilder()
                .url(authUrlBuilder.build())

        // If this is a POST request, api.stackexchange.com expects the access_token in the form body
        if (!accessToken.isNullOrBlank() && request.isPost()) {
            request.body?.addField(AuthStore.ACCESS_TOKEN, accessToken)?.let {
                authenticatedRequestBuilder.post(it)
            }
        }

        return chain.proceed(authenticatedRequestBuilder.build())
    }

    private fun Request.isStack() = baseUrl.contains(url.host, ignoreCase = true)

    private fun Request.isPost() = method.equals("post", ignoreCase = true)
}
