package me.tylerbwong.stack.data.auth

import me.tylerbwong.stack.data.auth.utils.addField
import me.tylerbwong.stack.data.utils.isPost
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val baseUrl: String,
    private val authStore: AuthStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // We do not want to add authentication to any request not going to api.stackexchange.com
        if (!request.isBaseUrl) {
            return chain.proceed(request)
        }

        val authUrlBuilder = request.url.newBuilder()
        val accessToken = authStore.accessToken
        if (!accessToken.isNullOrBlank() && !request.isPost) {
            authUrlBuilder.addEncodedQueryParameter(AuthStore.ACCESS_TOKEN, accessToken)
        }

        val authenticatedRequestBuilder = request.newBuilder()
            .url(authUrlBuilder.build())

        // If this is a POST request, api.stackexchange.com expects the access_token in the form body
        if (!accessToken.isNullOrBlank() && request.isPost) {
            request.body?.addField(AuthStore.ACCESS_TOKEN, accessToken)?.let {
                authenticatedRequestBuilder.post(it)
            }
        }

        return chain.proceed(authenticatedRequestBuilder.build())
    }

    private val Request.isBaseUrl get() = baseUrl.contains(url.host, ignoreCase = true)
}
