package me.tylerbwong.stack.ui.utils

import android.view.View
import me.tylerbwong.stack.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ViewUtilsTest : BaseTest() {
    @Test
    fun `view clicked rapidly multiple times will not get triggered more than once`() {
        var count = 0

        val view = View(context).apply {
            setThrottledOnClickListener {
                count++
            }
        }

        repeat(10) {
            view.performClick()
        }

        assertEquals(1, count)
    }
}
