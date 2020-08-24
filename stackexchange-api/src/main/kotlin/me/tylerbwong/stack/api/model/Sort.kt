package me.tylerbwong.stack.api.model

import androidx.annotation.StringDef
import androidx.annotation.StringRes
import me.tylerbwong.stack.api.R

const val SORT_PARAM = "sort"
const val CREATION = "creation"
const val ACTIVITY = "activity"
const val VOTES = "votes"
const val HOT = "hot"
const val WEEK = "week"
const val MONTH = "month"
const val RELEVANCE = "relevance"

@StringDef(CREATION, ACTIVITY, VOTES, HOT, WEEK, MONTH, RELEVANCE)
annotation class Sort

val String.sortResourceId: Int
    @StringRes
    get() = when (this) {
        CREATION -> R.string.creation
        ACTIVITY -> R.string.activity
        VOTES -> R.string.votes
        HOT -> R.string.hot
        WEEK -> R.string.week
        MONTH -> R.string.month
        RELEVANCE -> R.string.relevance
        else -> R.string.creation
    }
