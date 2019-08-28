package me.tylerbwong.stack.data.auth

import android.net.Uri
import me.tylerbwong.stack.BaseTest
import me.tylerbwong.stack.data.network.service.StackService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class AuthProviderTest : BaseTest() {

    @Mock
    private lateinit var service: StackService

    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        repository = AuthRepository(service = service)
        AuthProvider.accessToken = null
    }

    @Test
    fun `setAccessToken() with valid redirect uri sets correct access token`() {
        assertEquals(false, AuthProvider.isAuthenticatedLiveData.value)
        val validUri = Uri.parse("stack://tylerbwong.me/auth/redirect#access_token=1234567")
        AuthProvider.setAccessToken(validUri)
        assertEquals("1234567", AuthProvider.accessToken)
        assertEquals(true, AuthProvider.isAuthenticatedLiveData.value)
    }

    @Test
    fun `setAccessToken() with invalid redirect uri does not set access token`() {
        assertEquals(false, AuthProvider.isAuthenticatedLiveData.value)
        val invalidUri = Uri.parse("stack://tylerbwong.me/auth?access_token=1234567")
        AuthProvider.setAccessToken(invalidUri)
        assertNull(AuthProvider.accessToken)
        assertEquals(false, AuthProvider.isAuthenticatedLiveData.value)
    }
}
