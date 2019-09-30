package me.tylerbwong.stack.ui.utils

import com.soywiz.klock.DateTime
import com.soywiz.klock.days
import com.soywiz.klock.hours
import com.soywiz.klock.minutes
import com.soywiz.klock.months
import com.soywiz.klock.seconds
import com.soywiz.klock.weeks
import com.soywiz.klock.years
import me.tylerbwong.stack.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeInfoTest : BaseTest() {

    @Test
    fun `format the date time based on stack overflow asked user actions in a day`() {
        val now = DateTime.now()
        val fetchedDate = (now - 1.days).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 day ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a days`() {
        val now = DateTime.now()
        val fetchedDate = (now - 2.days).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 days ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a year`() {
        val now = DateTime.now()
        val fetchedDate = (now - 1.years).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 year ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a years`() {
        val now = DateTime.now()
        val fetchedDate = (now - 2.years).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 years ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow edited user actions in a month`() {
        val now = DateTime.now()
        val fetchedDate = (now - 1.months - 1.days).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 month ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow edited user actions in a months`() {
        val now = DateTime.now()
        val fetchedDate = (now - 2.months).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 months ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a week`() {
        val now = DateTime.now()
        val fetchedDate = (now - 1.weeks).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 week ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a weeks`() {
        val now = DateTime.now()
        val fetchedDate = (now - 2.weeks).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 weeks ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a hour`() {
        val now = DateTime.now()
        val fetchedDate = (now - 1.hours).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 hour ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a hours`() {
        val now = DateTime.now()
        val fetchedDate = (now - 2.hours).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 hours ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a minute`() {
        val now = DateTime.now()
        val fetchedDate = (now - 1.minutes).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 minute ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a minutes`() {
        val now = DateTime.now()
        val fetchedDate = (now - 2.minutes).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 minutes ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a seconds`() {
        val now = DateTime.now()
        val fetchedDate = (now - 2.seconds).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 seconds ago", elapsedTime)
    }
}
