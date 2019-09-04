package me.tylerbwong.stack.data.auth

import android.net.Uri
import me.tylerbwong.stack.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthStoreTest : BaseTest() {

    @Before
    fun setUp() {
        AuthStore.clear()
    }

    @Test
    fun `setAccessToken() with valid redirect uri sets correct access token`() {
        assertEquals(false, AuthStore.isAuthenticatedLiveData.value)
        val validUri = Uri.parse("stack://tylerbwong.me/auth/redirect#access_token=1234567")
        AuthStore.setAccessToken(validUri)
        assertEquals("1234567", AuthStore.accessToken)
        assertEquals(true, AuthStore.isAuthenticatedLiveData.value)
    }

    @Test
    fun `setAccessToken() with invalid redirect uri does not set access token`() {
        assertEquals(false, AuthStore.isAuthenticatedLiveData.value)
        val invalidUri = Uri.parse("stack://tylerbwong.me/auth?access_token=1234567")
        AuthStore.setAccessToken(invalidUri)
        assertNull(AuthStore.accessToken)
        assertEquals(false, AuthStore.isAuthenticatedLiveData.value)
    }
}
