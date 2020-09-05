package me.tylerbwong.stack.data.auth

import android.net.Uri
import me.tylerbwong.stack.BaseTest
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

class AuthInterceptorTest : BaseTest() {

    private val mockWebServer = MockWebServer()

    private lateinit var authStore: AuthStore
    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        authStore = AuthStore(testSharedPreferences)
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor("localhost", authStore))
            .build()

        val validUri =
            Uri.parse("stack://tylerbwong.me/auth/redirect#access_token=$TEST_ACCESS_TOKEN")
        authStore.setAccessToken(validUri)
    }

    @Test
    fun `appends token for get request`() {
        okHttpClient.newCall(
            Request.Builder()
                .url(mockWebServer.url("/"))
                .get()
                .build()
        ).execute()

        val request = mockWebServer.takeRequest()

        assertEquals(TEST_ACCESS_TOKEN, request.requestUrl?.queryParameter(ACCESS_TOKEN))
    }

    @Test
    fun `adds token to request body and does not append token as query parameter for post request`() {
        val testRequestBody = """{}""".trimIndent()
            .toRequestBody("application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull())
        okHttpClient.newCall(
            Request.Builder()
                .url(mockWebServer.url("/"))
                .post(testRequestBody)
                .build()
        ).execute()

        val request = mockWebServer.takeRequest()

        assertNull(request.requestUrl?.queryParameter(ACCESS_TOKEN))
        assertTrue(request.body.readUtf8().contains(TEST_ACCESS_TOKEN))
    }

    @Test
    fun `does not append token if it is blank`() {
        authStore.clear()
        okHttpClient.newCall(
            Request.Builder()
                .url(mockWebServer.url("/"))
                .get()
                .build()
        ).execute()

        val request = mockWebServer.takeRequest()

        assertNull(request.requestUrl?.queryParameter(ACCESS_TOKEN))
    }

    @Test
    fun `does not append token if request is not to baseUrl`() {
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor("https://google.com", authStore))
            .build()

        okHttpClient.newCall(
            Request.Builder()
                .url(mockWebServer.url("/"))
                .get()
                .build()
        ).execute()

        val request = mockWebServer.takeRequest()

        assertNull(request.requestUrl?.queryParameter(ACCESS_TOKEN))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    companion object {
        private const val TEST_ACCESS_TOKEN = "1234567890"
        private const val ACCESS_TOKEN = "access_token"
    }
}
