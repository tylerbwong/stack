package me.tylerbwong.stack.data.model

import androidx.annotation.StringDef

const val SORT_PARAM = "sort"
const val CREATION = "creation"
const val ACTIVITY = "activity"
const val VOTES = "votes"
const val HOT = "hot"
const val WEEK = "week"
const val MONTH = "month"
const val SETTINGS = "settings"
const val RELEVANCE = "relevance"

@StringDef(CREATION, ACTIVITY, VOTES, HOT, WEEK, MONTH, SETTINGS, RELEVANCE)
annotation class Sort
