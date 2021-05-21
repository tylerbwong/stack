@file:Suppress("MaxLineLength")
package me.tylerbwong.compose.markdown.visitors

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import me.tylerbwong.compose.markdown.builder.toMarkdownTextContent
import org.junit.Before
import org.junit.Test

class AnnotatedStringBuilderTest {

    private lateinit var typography: Typography
    private lateinit var visitor: HeaderVisitor

    @Before
    fun setUp() {
        typography = Typography()
        visitor = HeaderVisitor(typography)
    }

    @Test
    fun `checkbox span is parsed properly`() {
        val (
            annotatedString,
            inlineTextContent,
            linkPositions
        ) = "- [x] I am a checked checkbox".toMarkdownTextContent(typography)

        assertEquals(1, inlineTextContent.size)
        assertTrue(linkPositions.isEmpty())
        assertEquals("[x] I am a checked checkbox", annotatedString.text)
    }

    @Test
    fun `code span is parsed properly`() {
        val (
            annotatedString,
            inlineTextContent,
            linkPositions
        ) = "`test`".toMarkdownTextContent(typography)

        assertTrue(inlineTextContent.isEmpty())
        assertTrue(linkPositions.isEmpty())
        assertEquals("test", annotatedString.text)
        assertTrue(annotatedString.spanStyles.all { it.item.fontFamily == FontFamily.Monospace })
        assertTrue(
            annotatedString.spanStyles.all {
                it.item.background == Color.LightGray.copy(alpha = 0.5f)
            }
        )
    }

    @Test
    fun `emphasis is parsed properly`() {
        val (
            annotatedString,
            inlineTextContent,
            linkPositions
        ) = "_test_".toMarkdownTextContent(typography)

        assertTrue(inlineTextContent.isEmpty())
        assertTrue(linkPositions.isEmpty())
        assertEquals("test", annotatedString.text)
        assertTrue(annotatedString.spanStyles.all { it.item.fontStyle == FontStyle.Italic })
    }

    @Test
    fun `headers are parsed properly`() {
        mapOf(
            "# I am an h1 heading" to typography.h1,
            "## I am an h2 heading" to typography.h2,
            "### I am an h3 heading" to typography.h3,
            "#### I am an h4 heading" to typography.h4,
            "##### I am an h5 heading" to typography.h5,
            "###### I am an h6 heading" to typography.h6
        ).forEach { (rawHeading, textStyle) ->
            val (
                annotatedString,
                inlineTextContent,
                linkPositions
            ) = rawHeading.toMarkdownTextContent(typography)
            assertTrue(inlineTextContent.isEmpty())
            assertTrue(linkPositions.isEmpty())
            assertEquals(
                rawHeading.dropWhile { it in listOf('#', ' ') },
                annotatedString.text
            )
            assertTrue(annotatedString.spanStyles.all { it.item == textStyle.toSpanStyle() })
        }
    }

    @Test
    fun `image span is parsed properly`() {
        val (
            annotatedString,
            inlineTextContent,
            linkPositions
        ) = "Inline images are supported as well ![Kotlin](https://developer.android.com/images/jetpack/info-bytes-compose-less-code.png)!".toMarkdownTextContent(typography)

        assertEquals(1, inlineTextContent.size)
        assertTrue(linkPositions.isEmpty())
        assertEquals("Inline images are supported as well https://developer.android.com/images/jetpack/info-bytes-compose-less-code.png!", annotatedString.text)
    }

    @Test
    fun `link span is parsed properly`() {
        val (
            annotatedString,
            inlineTextContent,
            linkPositions
        ) = "Inline [links](https://google.com) are now also supported!".toMarkdownTextContent(typography)

        assertTrue(inlineTextContent.isEmpty())
        assertEquals(1, linkPositions.size)
        assertEquals("Inline links are now also supported!", annotatedString.text)
    }

    @Test
    fun `strikethrough is parsed properly`() {
        val (
            annotatedString,
            inlineTextContent,
            linkPositions
        ) = "~~test~~".toMarkdownTextContent(typography)

        assertTrue(inlineTextContent.isEmpty())
        assertTrue(linkPositions.isEmpty())
        assertEquals("test", annotatedString.text)
        assertTrue(
            annotatedString.spanStyles.all { it.item.textDecoration == TextDecoration.LineThrough }
        )
    }

    @Test
    fun `strong is parsed properly`() {
        val (
            annotatedString,
            inlineTextContent,
            linkPositions
        ) = "**test**".toMarkdownTextContent(typography)

        assertTrue(inlineTextContent.isEmpty())
        assertTrue(linkPositions.isEmpty())
        assertEquals("test", annotatedString.text)
        assertTrue(
            annotatedString.spanStyles.all { it.item.fontWeight == FontWeight.Bold }
        )
    }
}
