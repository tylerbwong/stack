package me.tylerbwong.stack.ui.utils

import android.widget.TextView
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
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 1.days).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("asked 1 day ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a days`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 2.days).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("asked 2 days ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a year`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 1.years).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("asked 1 year ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a years`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 2.years).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("asked 2 years ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow edited user actions in a month`() {
        val actionType = UserActionType.EDITED
        val fetchedDate = (now - 1.months).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("edited 1 month ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow edited user actions in a months`() {
        val actionType = UserActionType.EDITED
        val fetchedDate = (now - 2.months).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("edited 2 months ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a week`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 1.weeks).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("asked 1 week ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a weeks`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 2.weeks).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("asked 2 weeks ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a hour`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 1.hours).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("asked 1 hour ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a hours`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 2.hours).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("asked 2 hours ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a minute`() {
        val actionType = UserActionType.ANSWERED
        val fetchedDate = (now - 1.minutes).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("answered 1 minute ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a minutes`() {
        val actionType = UserActionType.ANSWERED
        val fetchedDate = (now - 2.minutes).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("answered 2 minutes ago", textView)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a seconds`() {
        val actionType = UserActionType.ANSWERED
        val fetchedDate = (now - 2.seconds).unixMillisLong

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        assertEquals("answered 2 seconds ago", textView)
    }
}