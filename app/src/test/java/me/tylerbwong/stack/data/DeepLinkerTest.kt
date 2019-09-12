package me.tylerbwong.stack.data

import android.net.Uri
import me.tylerbwong.stack.BaseTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class DeepLinkerTest : BaseTest() {

    private val unsupportedDeepLinks = listOf(
        "https://stackoverflow.com/search?q=android+toolbar",
        "https://stackoverflow.com/answers"
    ).map { Uri.parse(it) }

    private val supportedDeepLinks = listOf(
        "stack://tylerbwong.me/auth/redirect#access_token=abcdefg",
        "https://stackoverflow.com/questions/tagged/android",
        "http://stackoverflow.com/questions/26533510/",
        "http://stackoverflow.com/q/26533510/"
    ).map { Uri.parse(it) }

    @Test
    fun `resolveUri returns null value for invalid deep links`() {
        unsupportedDeepLinks.forEach {
            assertNull(DeepLinker.resolvePath(context, it))
        }
    }

    @Test
    fun `resolveUri returns non-null value for valid deep links`() {
        supportedDeepLinks.forEach {
            assertNotNull(DeepLinker.resolvePath(context, it))
        }
    }
}
