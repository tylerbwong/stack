package me.tylerbwong.stack.data.site

import me.tylerbwong.stack.BaseTest
import me.tylerbwong.stack.api.service.DEFAULT_SITE
import me.tylerbwong.stack.api.service.SITE_PARAM
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SiteInterceptorTest : BaseTest() {

    private val mockWebServer = MockWebServer()

    private lateinit var siteStore: SiteStore
    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        siteStore = SiteStore(testSharedPreferences)
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(SiteInterceptor("localhost", siteStore))
            .build()
    }

    @Test
    fun `appends site for get request`() {
        okHttpClient.newCall(
            Request.Builder()
                .url(mockWebServer.url("/"))
                .get()
                .build()
        ).execute()

        val request = mockWebServer.takeRequest()

        assertEquals(DEFAULT_SITE, request.requestUrl?.queryParameter(SITE_PARAM))
    }

    @Test
    fun `adds site to request body and does not append site as query parameter for post request`() {
        val testRequestBody = """{}""".trimIndent()
            .toRequestBody("application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull())
        okHttpClient.newCall(
            Request.Builder()
                .url(mockWebServer.url("/"))
                .post(testRequestBody)
                .build()
        ).execute()

        val request = mockWebServer.takeRequest()

        assertNull(request.requestUrl?.queryParameter(SITE_PARAM))
        assertTrue(request.body.readUtf8().contains(DEFAULT_SITE))
    }

    @Test
    fun `does not append site if request is not to baseUrl`() {
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(SiteInterceptor("https://google.com", siteStore))
            .build()

        okHttpClient.newCall(
            Request.Builder()
                .url(mockWebServer.url("/"))
                .get()
                .build()
        ).execute()

        val request = mockWebServer.takeRequest()

        assertNull(request.requestUrl?.queryParameter(SITE_PARAM))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
