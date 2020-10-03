package me.tylerbwong.stack.data.auth

import me.tylerbwong.stack.api.di.BaseUrl
import me.tylerbwong.stack.data.auth.utils.addField
import me.tylerbwong.stack.data.utils.isPost
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    @BaseUrl private val baseUrl: String,
    private val authStore: AuthStore
) : Interceptor {

    private val Request.isBaseUrl: Boolean
        get() = baseUrl.contains(url.host, ignoreCase = true)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = authStore.accessToken

        // We do not want to add authentication to any request not going to api.stackexchange.com
        if (!request.isBaseUrl || accessToken.isNullOrBlank()) {
            return chain.proceed(request)
        }

        val authenticatedRequestBuilder = request.newBuilder()
        return chain.proceed(
            // If this is a POST request, api.stackexchange.com expects the access_token in the form body
            if (request.isPost) {
                request.body?.addField(AuthStore.ACCESS_TOKEN, accessToken)?.let {
                    authenticatedRequestBuilder.post(it)
                }
                authenticatedRequestBuilder.build()
            } else {
                val authUrlBuilder = request.url.newBuilder()
                authUrlBuilder.addEncodedQueryParameter(AuthStore.ACCESS_TOKEN, accessToken)
                authenticatedRequestBuilder.url(authUrlBuilder.build()).build()
            }
        )
    }
}
