package me.tylerbwong.stack.data.auth

import android.net.Uri
import kotlinx.coroutines.runBlocking
import me.tylerbwong.stack.BaseTest
import me.tylerbwong.stack.data.network.service.StackService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class AuthProviderTest : BaseTest() {

    @Mock
    private lateinit var service: StackService

    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = AuthRepository(service = service)
        val testUri = Uri.parse("stack://tylerbwong.me/auth/redirect#access_token=1234567")
        AuthProvider.setAccessToken(testUri)
    }

    @Test
    fun `setAccessToken() with redirect uri sets correct access token`() {
        assertEquals("1234567", AuthProvider.accessToken)
    }

    @Test
    fun `logOut() clears access token`() {
        assertFalse(AuthProvider.accessToken.isNullOrBlank())
        runBlocking { repository.logOut() }
        assertTrue(AuthProvider.accessToken.isNullOrBlank())
        runBlocking { verify(service).logOut("1234567") }
    }
}
