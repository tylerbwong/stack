package me.tylerbwong.stack.ui.utils

import java.util.*

const val CALC_YEAR = 1
const val CALC_MONTH = 2
const val CALC_DAY = 3
const val CALC_HOUR = 4
const val CALC_MINUTES = 5

internal fun calendarCalculation(minutesBefore: Long, typeDate: Int): Int {
    val calendarAux = Calendar.getInstance()
    calendarAux.timeInMillis = minutesBefore

    return when (typeDate) {
        CALC_YEAR -> {
            val year = calendarAux.get(Calendar.YEAR)
            Calendar.getInstance().get(Calendar.YEAR) - year
        }
        CALC_MONTH -> {
            val month = calendarAux.get(Calendar.MONTH)
            Calendar.getInstance().get(Calendar.MONTH) - month
        }
        CALC_DAY -> {
            val day = calendarAux.get(Calendar.DAY_OF_MONTH)
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - day
        }
        CALC_HOUR -> {
            val hour = calendarAux.get(Calendar.HOUR_OF_DAY)
            Calendar.getInstance().get(Calendar.MINUTE) - hour
        }
        else -> {
            val minute = calendarAux.get(Calendar.MINUTE)
            Calendar.getInstance().get(Calendar.MINUTE) - minute
        }
    }
}

internal fun getViewsCount(viewCount: Int): String {
    val viewCountString = viewCount.toString()
    val viewCountLength = viewCountString.length

    return when {
        viewCountLength > 3 -> "${viewCountString.removeRange(viewCountLength - 3, viewCountLength)}K"
        else -> viewCountString
    }
}