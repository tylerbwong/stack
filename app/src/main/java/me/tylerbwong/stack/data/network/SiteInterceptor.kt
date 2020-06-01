package me.tylerbwong.stack.data.network

import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.auth.utils.addField
import me.tylerbwong.stack.data.network.service.SITE_PARAM
import me.tylerbwong.stack.data.utils.isPost
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class SiteInterceptor @Inject constructor(
    private val baseUrl: String,
    private val siteStore: SiteStore = SiteStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // We do not want to add site to any request not going to api.stackexchange.com
        if (!request.isBaseUrl || request.url.encodedPathSegments.any { it in blacklist }) {
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

    private val Request.isBaseUrl get() = baseUrl.contains(url.host, ignoreCase = true)

    companion object {
        // These endpoints do not accept a site parameter
        private val blacklist = listOf(
            "access-tokens",
            "sites"
        )
    }
}
