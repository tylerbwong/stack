package me.tylerbwong.stack.ui.questions

import androidx.annotation.StringRes
import me.tylerbwong.stack.R

enum class QuestionPage(@StringRes val titleRes: Int? = null) {
    TAGS, // Tags page will use the tag itself as the title
    LINKED(R.string.linked),
    RELATED(R.string.related);
}
