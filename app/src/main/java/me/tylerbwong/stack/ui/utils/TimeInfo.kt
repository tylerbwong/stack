package me.tylerbwong.stack.ui.utils

import android.view.View
import com.soywiz.klock.DateTime
import com.soywiz.klock.until
import me.tylerbwong.stack.R

enum class Type {
    ASKED,
    ANSWERED,
    EDITED
}

fun Long.type(view: View, type: Type): String =
    when (type) {
        Type.ASKED -> view.resources.getQuantityString(R.plurals.type, 0)
        Type.ANSWERED -> view.resources.getQuantityString(R.plurals.type, 1)
        Type.EDITED -> view.resources.getQuantityString(R.plurals.type, 2)
    }

fun String.time(view: View, creation_date: Long): String {
    val creationDate = DateTime.fromUnix(creation_date)
    val dateCalculation = (DateTime.now() until creationDate).span

    return when {
        dateCalculation.minutes <= 5 -> view.resources.getQuantityString(
            R.plurals.min,
            0,
            dateCalculation.minutes
        )
        dateCalculation.hours < 1 -> view.resources.getQuantityString(
            R.plurals.min,
            1,
            dateCalculation.minutes
        )
        dateCalculation.hours == 1 -> view.resources.getQuantityString(
            R.plurals.hour,
            0,
            dateCalculation.hours
        )
        dateCalculation.days < 1 -> view.resources.getQuantityString(
            R.plurals.hour,
            1,
            dateCalculation.hours
        )
        dateCalculation.days == 1 -> view.resources.getQuantityString(
            R.plurals.day,
            0,
            dateCalculation.days
        )
        dateCalculation.weeks < 1 -> view.resources.getQuantityString(
            R.plurals.day,
            1,
            dateCalculation.days
        )
        dateCalculation.weeks == 1 -> view.resources.getQuantityString(
            R.plurals.week,
            0,
            dateCalculation.weeks
        )
        dateCalculation.months < 1 -> view.resources.getQuantityString(
            R.plurals.week,
            1,
            dateCalculation.weeks
        )
        dateCalculation.months == 1 -> view.resources.getQuantityString(
            R.plurals.month,
            0,
            dateCalculation.months
        )
        dateCalculation.years < 1 -> view.resources.getQuantityString(
            R.plurals.month,
            1,
            dateCalculation.months
        )
        dateCalculation.years == 1 -> view.resources.getQuantityString(
            R.plurals.year,
            0,
            dateCalculation.years
        )
        else -> view.resources.getQuantityString(
            R.plurals.year,
            1,
            dateCalculation.years
        )
    }
}
