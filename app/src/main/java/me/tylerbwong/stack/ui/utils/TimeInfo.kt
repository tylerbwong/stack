package me.tylerbwong.stack.ui.utils

import android.content.Context
import com.soywiz.klock.DateTime
import com.soywiz.klock.milliseconds
import com.soywiz.klock.until
import me.tylerbwong.stack.R

fun Long.formatElapsedTime(context: Context): String {
    val creationDate = DateTime.fromUnix(this.milliseconds.millisecondsLong)
    val dateCalculation = (creationDate until DateTime.now()).span

    return when {
        dateCalculation.years >= 1 -> context.resources.getQuantityString(
            R.plurals.year,
            dateCalculation.years,
            dateCalculation.years
        )
        dateCalculation.months >= 1 -> context.resources.getQuantityString(
            R.plurals.month,
            dateCalculation.months,
            dateCalculation.months
        )
        dateCalculation.weeks >= 1 -> context.resources.getQuantityString(
            R.plurals.week,
            dateCalculation.weeks,
            dateCalculation.weeks
        )
        dateCalculation.days >= 1 -> context.resources.getQuantityString(
            R.plurals.day,
            dateCalculation.days,
            dateCalculation.days
        )
        dateCalculation.hours >= 1 -> context.resources.getQuantityString(
            R.plurals.hour,
            dateCalculation.hours,
            dateCalculation.hours
        )
        dateCalculation.minutes >= 1 -> context.resources.getQuantityString(
            R.plurals.minute,
            dateCalculation.minutes,
            dateCalculation.minutes
        )
        else -> context.resources.getString(R.string.seconds, dateCalculation.seconds)
    }
}
