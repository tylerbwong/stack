package me.tylerbwong.stack.ui.utils

import android.widget.TextView
import androidx.annotation.StringRes
import com.soywiz.klock.DateTime
import com.soywiz.klock.milliseconds
import com.soywiz.klock.until
import me.tylerbwong.stack.R

enum class UserActionType(@StringRes val labelResourceId: Int) {
    ASKED(R.string.asked),
    ANSWERED(R.string.answered),
    EDITED(R.string.edited)
}

fun TextView.formatTimeForActionType(actionType: UserActionType, timeStamp: Long): String {
    val creationDate = DateTime.fromUnix(timeStamp.milliseconds.millisecondsLong)
    val dateCalculation = (creationDate until DateTime.now()).span

    val userAction = when (actionType) {
        UserActionType.ASKED -> context.getString(UserActionType.ASKED.labelResourceId)
        UserActionType.ANSWERED -> context.getString(UserActionType.ANSWERED.labelResourceId)
        UserActionType.EDITED -> context.getString(UserActionType.EDITED.labelResourceId)
    }

    val timeUserAction = when {
        dateCalculation.years > 1 -> this.resources.getQuantityString(
            R.plurals.year,
            dateCalculation.years,
            dateCalculation.years
        )
        dateCalculation.years == 1 -> this.resources.getQuantityString(
            R.plurals.year,
            dateCalculation.years,
            dateCalculation.years
        )
        dateCalculation.months > 1 -> this.resources.getQuantityString(
            R.plurals.month,
            dateCalculation.months,
            dateCalculation.months
        )
        dateCalculation.months == 1 -> this.resources.getQuantityString(
            R.plurals.month,
            dateCalculation.months,
            dateCalculation.months
        )
        dateCalculation.weeks > 1 -> this.resources.getQuantityString(
            R.plurals.week,
            dateCalculation.weeks,
            dateCalculation.weeks
        )
        dateCalculation.weeks == 1 -> this.resources.getQuantityString(
            R.plurals.week,
            dateCalculation.weeks,
            dateCalculation.weeks
        )
        dateCalculation.days > 1 -> this.resources.getQuantityString(
            R.plurals.day,
            dateCalculation.days,
            dateCalculation.days
        )
        dateCalculation.days == 1 -> this.resources.getQuantityString(
            R.plurals.day,
            dateCalculation.days,
            dateCalculation.days
        )
        dateCalculation.hours > 1 -> this.resources.getQuantityString(
            R.plurals.hour,
            dateCalculation.hours,
            dateCalculation.hours
        )
        dateCalculation.hours == 1 -> this.resources.getQuantityString(
            R.plurals.hour,
            dateCalculation.hours,
            dateCalculation.hours
        )
        dateCalculation.minutes > 1 -> this.resources.getQuantityString(
            R.plurals.min,
            dateCalculation.minutes,
            dateCalculation.minutes
        )
        dateCalculation.minutes == 1 -> this.resources.getQuantityString(
            R.plurals.min,
            dateCalculation.minutes,
            dateCalculation.minutes
        )
        else -> this.resources.getString(R.string.seconds, dateCalculation.seconds)
    }

    return "$userAction $timeUserAction"

}
