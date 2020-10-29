package me.tylerbwong.stack.data.site

import me.tylerbwong.stack.api.di.BaseUrl
import me.tylerbwong.stack.api.service.SITE_PARAM
import me.tylerbwong.stack.data.auth.utils.addField
import me.tylerbwong.stack.data.utils.isPost
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class SiteInterceptor @Inject constructor(
    @BaseUrl private val baseUrl: String,
    private val siteStore: SiteStore
) : Interceptor {

    private val Request.isBaseUrl: Boolean
        get() = baseUrl.contains(url.host, ignoreCase = true)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // We do not want to add site to any request not going to api.stackexchange.com
        if (!request.isBaseUrl || request.url.encodedPathSegments.any { it in unsupportedEndpoints }) {
            return chain.proceed(request)
        }

        val siteRequestBuilder = request.newBuilder()
        return chain.proceed(
            if (request.isPost) {
                request.body?.addField(SITE_PARAM, siteStore.site)?.let {
                    siteRequestBuilder.post(it)
                }
                siteRequestBuilder.build()
            } else {
                val siteUrlBuilder = request.url.newBuilder()
                siteUrlBuilder.addEncodedQueryParameter(SITE_PARAM, siteStore.site)
                siteRequestBuilder.url(siteUrlBuilder.build()).build()
            }
        )
    }

    companion object {
        // These endpoints do not accept a site parameter
        private val unsupportedEndpoints = listOf(
            "access-tokens",
            "sites"
        )
    }
}
