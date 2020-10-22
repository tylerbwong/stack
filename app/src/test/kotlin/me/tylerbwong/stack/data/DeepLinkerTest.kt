package me.tylerbwong.stack.data

import android.net.Uri
import me.tylerbwong.stack.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeepLinkerTest : BaseTest() {

    private lateinit var deepLinker: DeepLinker

    private val unsupportedDeepLinks = listOf(
        "https://stackoverflow.com/search?q=android+toolbar",
        "https://stackoverflow.com/answers"
    ).map { Uri.parse(it) }

    private val supportedDeepLinks = listOf(
        "https://stackoverflow.com/questions/tagged/android",
        "http://stackoverflow.com/questions/26533510/",
        "http://stackoverflow.com/q/26533510/",
        "https://sustainability.meta.stackexchange.com/questions/371/",
        "https://superuser.com/q/1491979/"
    ).map { Uri.parse(it) }

    private val supportedAuthUrls = listOf(
        "stack://tylerbwong.me/auth/redirect#access_token=abcdefg"
    ).map { Uri.parse(it) }

    @Before
    fun setUp() {
        deepLinker = DeepLinker()
    }

    @Test
    fun `resolveUri returns path not supported error for invalid deep links`() {
        unsupportedDeepLinks.forEach {
            val result = deepLinker.resolvePath(context, it)
            assertEquals(DeepLinkResult.PathNotSupportedError, result)
        }
    }

    @Test
    fun `resolveUri returns success for valid deep links`() {
        supportedDeepLinks.forEach {
            val result = deepLinker.resolvePath(context, it)
            assertTrue(result is DeepLinkResult.Success)
        }
    }

    @Test
    fun `resolveUri returns requesting auth for valid auth deep links`() {
        supportedAuthUrls.forEach {
            val result = deepLinker.resolvePath(context, it)
            assertTrue(result is DeepLinkResult.RequestingAuth)
        }
    }

    @Test
    fun `resolveUri with tagged path returns site mismatch error if site is not current site`() {
        val uri = Uri.parse("https://superuser.com/questions/tagged/android")
        val result = deepLinker.resolvePath(context, uri)
        assertTrue(result is DeepLinkResult.Success)
    }
}
