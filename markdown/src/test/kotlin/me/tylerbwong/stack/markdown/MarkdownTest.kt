package me.tylerbwong.stack.markdown

import android.widget.TextView
import io.noties.markwon.Markwon
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MarkdownTest {

    @Test
    fun `test markdown is set`() {
        val mockMarkwon = mock<Markwon>()
        val textView = mock<TextView>()
        Markdown(mockMarkwon).setMarkdown(textView, "test")
        verify(mockMarkwon, times(1)).setMarkdown(textView, "test")
    }
}
