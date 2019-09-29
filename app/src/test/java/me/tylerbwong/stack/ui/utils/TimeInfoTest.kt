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

    private val now = DateTime.now()

    @Test
    fun `format the date time based on stack overflow asked user actions in a day`() {
        val fetchedDate = (now - 1.days).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 day ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a days`() {
        val fetchedDate = (now - 2.days).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 days ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a year`() {
        val fetchedDate = (now - 1.years).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 year ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a years`() {
        val fetchedDate = (now - 2.years).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 years ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow edited user actions in a month`() {
        val fetchedDate = (now - 1.months).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 month ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow edited user actions in a months`() {
        val fetchedDate = (now - 2.months).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 months ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a week`() {
        val fetchedDate = (now - 1.weeks).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 week ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a weeks`() {
        val fetchedDate = (now - 2.weeks).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 weeks ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a hour`() {
        val fetchedDate = (now - 1.hours).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 hour ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a hours`() {
        val fetchedDate = (now - 2.hours).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 hours ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a minute`() {
        val fetchedDate = (now - 1.minutes).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("1 minute ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a minutes`() {
        val fetchedDate = (now - 2.minutes).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 minutes ago", elapsedTime)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a seconds`() {
        val fetchedDate = (now - 2.seconds).unixMillisLong

        val elapsedTime = fetchedDate.formatElapsedTime(context)

        assertEquals("2 seconds ago", elapsedTime)
    }
}