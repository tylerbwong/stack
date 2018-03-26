package me.tylerbwong.stack.data.model

import android.support.annotation.StringDef

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
