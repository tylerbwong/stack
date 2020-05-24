package me.tylerbwong.stack.ui.search.filters

import android.content.Context
import me.tylerbwong.stack.R

sealed class Filter {

    open fun getLabel(context: Context): String = ""

    data class Accepted(val isAccepted: Boolean) : Filter() {
        override fun getLabel(context: Context) = context.getString(
            if (isAccepted) {
                R.string.has_accepted_answer
            } else {
                R.string.no_accepted_answers
            }
        )
    }
    data class MinAnswers(val minNumAnswers: Int) : Filter() {
        override fun getLabel(context: Context) = context.resources.getQuantityString(
            R.plurals.has_min_answers,
            minNumAnswers,
            minNumAnswers
        )
    }
    data class BodyContains(val bodyContains: String) : Filter() {
        override fun getLabel(context: Context) = context.getString(
            R.string.body_contains,
            bodyContains
        )
    }
    data class Closed(val isClosed: Boolean) : Filter() {
        override fun getLabel(context: Context) = context.getString(
            if (isClosed) {
                R.string.is_closed
            } else {
                R.string.is_open
            }
        )
    }
    data class Tags(val tags: List<String>) : Filter() {
        override fun getLabel(context: Context) = context.getString(
            R.string.tags_filter,
            tags.joinToString(", ")
        )
    }
    data class TitleContains(val titleContains: String) : Filter() {
        override fun getLabel(context: Context) = context.getString(
            R.string.title_contains,
            titleContains
        )
    }

    object None : Filter()
}
