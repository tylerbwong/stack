package me.tylerbwong.stack.data.auth

import android.net.Uri
import me.tylerbwong.stack.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthStoreTest : BaseTest() {

    private lateinit var authStore: AuthStore

    @Before
    fun setUp() {
        authStore = stackComponent.authStore()
        authStore.clear()
    }

    @Test
    fun `setAccessToken() with valid redirect uri sets correct access token`() {
        assertEquals(false, authStore.isAuthenticatedLiveData.value)
        val validUri = Uri.parse("stack://tylerbwong.me/auth/redirect#access_token=1234567")
        authStore.setAccessToken(validUri)
        assertEquals("1234567", authStore.accessToken)
        assertEquals(true, authStore.isAuthenticatedLiveData.value)
    }

    @Test
    fun `setAccessToken() with invalid redirect uri does not set access token`() {
        assertEquals(false, authStore.isAuthenticatedLiveData.value)
        val invalidUri = Uri.parse("stack://tylerbwong.me/auth?access_token=1234567")
        authStore.setAccessToken(invalidUri)
        assertNull(authStore.accessToken)
        assertEquals(false, authStore.isAuthenticatedLiveData.value)
    }
}
