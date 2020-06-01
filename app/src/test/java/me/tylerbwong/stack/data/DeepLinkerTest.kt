package me.tylerbwong.stack.data

import android.net.Uri
import me.tylerbwong.stack.BaseTest
import me.tylerbwong.stack.data.network.service.SITE_PARAM
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class DeepLinkerTest : BaseTest() {

    private lateinit var deepLinker: DeepLinker

    private val unsupportedDeepLinks = listOf(
        "https://stackoverflow.com/search?q=android+toolbar",
        "https://stackoverflow.com/answers"
    ).map { Uri.parse(it) }

    private val supportedDeepLinks = listOf(
        "stack://tylerbwong.me/auth/redirect#access_token=abcdefg",
        "https://stackoverflow.com/questions/tagged/android",
        "http://stackoverflow.com/questions/26533510/",
        "http://stackoverflow.com/q/26533510/",
        "https://sustainability.meta.stackexchange.com/questions/371/",
        "https://superuser.com/q/1491979/"
    ).map { Uri.parse(it) }

    private val supportedDeepLinksWithSites = mapOf(
        "stackoverflow" to "https://stackoverflow.com/questions/tagged/android",
        "stackoverflow" to "http://stackoverflow.com/questions/26533510/",
        "stackoverflow" to "http://stackoverflow.com/q/26533510/",
        "sustainability.meta" to "https://sustainability.meta.stackexchange.com/questions/371/",
        "superuser" to "https://superuser.com/q/1491979/"
    ).entries.associate { it.key to Uri.parse(it.value) }

    @Before
    fun setUp() {
        deepLinker = stackComponent.deepLinker()
    }

    @Test
    fun `resolveUri returns null value for invalid deep links`() {
        unsupportedDeepLinks.forEach {
            assertNull(deepLinker.resolvePath(context, it))
        }
    }

    @Test
    fun `resolveUri returns non-null value for valid deep links`() {
        supportedDeepLinks.forEach {
            assertNotNull(deepLinker.resolvePath(context, it))
        }
    }

    @Test
    fun `resolveUri returns non-null value for valid deep links and correct site`() {
        supportedDeepLinksWithSites.forEach { (site, uri) ->
            val intent = deepLinker.resolvePath(context, uri)
            assertNotNull(intent)
            assertEquals(site, intent!!.getStringExtra(SITE_PARAM))
        }
    }

    @Test
    fun `resolveUri with tagged path returns null if site is not current site`() {
        val uri = Uri.parse("https://superuser.com/questions/tagged/android")
        assertNull(deepLinker.resolvePath(context, uri))
    }
}
