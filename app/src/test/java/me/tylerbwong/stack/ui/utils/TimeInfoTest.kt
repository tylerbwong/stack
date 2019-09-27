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
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "asked 1 day ago"

        assertEquals(txtView.text, textView)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a days`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 2.days).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "asked 2 days ago"

        assertEquals(txtView.text, textView)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a year`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 1.years).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "asked 1 year ago"

        assertEquals(textView, txtView.text)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a years`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 2.years).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "asked 2 years ago"

        assertEquals(textView, txtView.text)
    }

    @Test
    fun `format the date time based on stack overflow edited user actions in a month`() {
        val actionType = UserActionType.EDITED
        val fetchedDate = (now - 1.months).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "edited 1 month ago"

        assertEquals(textView, txtView.text)
    }

    @Test
    fun `format the date time based on stack overflow edited user actions in a months`() {
        val actionType = UserActionType.EDITED
        val fetchedDate = (now - 2.months).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "edited 2 months ago"

        assertEquals(textView, txtView.text)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a week`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 1.weeks).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "asked 1 week ago"

        assertEquals(textView, txtView.text)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a weeks`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 2.weeks).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "asked 2 weeks ago"

        assertEquals(textView, txtView.text)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a hour`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 1.hours).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "asked 1 hour ago"

        assertEquals(textView, txtView.text)
    }

    @Test
    fun `format the date time based on stack overflow asked user actions in a hours`() {
        val actionType = UserActionType.ASKED
        val fetchedDate = (now - 2.hours).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "asked 2 hours ago"

        assertEquals(textView, txtView.text)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a minute`() {
        val actionType = UserActionType.ANSWERED
        val fetchedDate = (now - 1.minutes).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "answered 1 minute ago"

        assertEquals(textView, txtView.text)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a minutes`() {
        val actionType = UserActionType.ANSWERED
        val fetchedDate = (now - 2.minutes).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "answered 2 minutes ago"

        assertEquals(textView, txtView.text)
    }

    @Test
    fun `format the date time based on stack overflow answered user actions in a seconds`() {
        val actionType = UserActionType.ANSWERED
        val fetchedDate = (now - 2.seconds).unixMillisLong
        val txtView = TextView(context)

        val textView = TextView(context).formatTimeForActionType(actionType, fetchedDate)

        txtView.text = "answered 2 seconds ago"

        assertEquals(textView, txtView.text)
    }
}