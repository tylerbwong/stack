package me.tylerbwong.stack.ui.utils

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.soywiz.klock.DateTime
import com.soywiz.klock.until
import me.tylerbwong.stack.R

enum class UserActionType(@StringRes val labelResourceId: Int) {
    ASKED(R.string.asked),
    ANSWERED(R.string.answered),
    EDITED(R.string.edited)
}

fun type(view: View, type: UserActionType) {
    when (type) {
        UserActionType.ASKED -> UserActionType.ASKED.labelResourceId
        UserActionType.ANSWERED -> UserActionType.ANSWERED.labelResourceId
        UserActionType.EDITED -> UserActionType.EDITED.labelResourceId
    }
}

fun TextView.formatTimeForActionType(actionType: UserActionType, timeStamp: Long) {
    val creationDate = DateTime.fromUnix(timeStamp * 1000)
    val dateCalculation = (creationDate until DateTime.now()).span

    when {
        dateCalculation.years > 1 -> this.resources.getQuantityString(
            R.plurals.year,
            1,
            dateCalculation.years
        )
        dateCalculation.years == 1 -> this.resources.getQuantityString(
            R.plurals.year,
            0,
            dateCalculation.years
        )
        dateCalculation.months > 1 -> this.resources.getQuantityString(
            R.plurals.month,
            1,
            dateCalculation.months
        )
        dateCalculation.months == 1 -> this.resources.getQuantityString(
            R.plurals.month,
            0,
            dateCalculation.months
        )
        dateCalculation.weeks > 1 -> this.resources.getQuantityString(
            R.plurals.week,
            1,
            dateCalculation.weeks
        )
        dateCalculation.weeks == 1 -> this.resources.getQuantityString(
            R.plurals.week,
            0,
            dateCalculation.weeks
        )
        dateCalculation.days > 1 -> this.resources.getQuantityString(
            R.plurals.day,
            1,
            dateCalculation.days
        )
        dateCalculation.days == 1 -> this.resources.getQuantityString(
            R.plurals.day,
            0,
            dateCalculation.days
        )
        dateCalculation.hours > 1 -> this.resources.getQuantityString(
            R.plurals.hour,
            1,
            dateCalculation.hours
        )
        dateCalculation.hours == 1 -> this.resources.getQuantityString(
            R.plurals.hour,
            0,
            dateCalculation.hours
        )
        dateCalculation.minutes > 5 -> this.resources.getQuantityString(
            R.plurals.min,
            1,
            dateCalculation.minutes
        )
        dateCalculation.minutes == 1 -> this.resources.getQuantityString(
            R.plurals.min,
            0,
            dateCalculation.minutes
        )
        else -> this.resources.getString(R.string.seconds, dateCalculation.seconds)
    }
}
