package me.tylerbwong.stack.ui.utils

import android.content.Context
import com.soywiz.klock.DateTime
import com.soywiz.klock.milliseconds
import com.soywiz.klock.until
import me.tylerbwong.stack.R

/**
 * Receiver [Long] is in milliseconds.
 */
fun Long.formatElapsedTime(context: Context): String {
    val startDate = DateTime.fromUnix(toDouble().milliseconds.millisecondsLong)
    val elapsedTime = (startDate until DateTime.now()).span
    val resources = context.resources

    return when {
        elapsedTime.years >= 1 -> resources.getQuantityString(
            R.plurals.year,
            elapsedTime.years,
            elapsedTime.years
        )
        elapsedTime.months >= 1 -> resources.getQuantityString(
            R.plurals.month,
            elapsedTime.months,
            elapsedTime.months
        )
        elapsedTime.weeks >= 1 -> resources.getQuantityString(
            R.plurals.week,
            elapsedTime.weeks,
            elapsedTime.weeks
        )
        elapsedTime.days >= 1 -> resources.getQuantityString(
            R.plurals.day,
            elapsedTime.days,
            elapsedTime.days
        )
        elapsedTime.hours >= 1 -> resources.getQuantityString(
            R.plurals.hour,
            elapsedTime.hours,
            elapsedTime.hours
        )
        elapsedTime.minutes >= 1 -> resources.getQuantityString(
            R.plurals.minute,
            elapsedTime.minutes,
            elapsedTime.minutes
        )
        else -> resources.getQuantityString(
            R.plurals.seconds,
            elapsedTime.seconds,
            elapsedTime.seconds
        )
    }
}
