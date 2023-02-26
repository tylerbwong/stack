package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.runtime.Composable
import me.tylerbwong.stack.api.model.Tag
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Details.MIN_DETAILS_LENGTH
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Title.TITLE_LENGTH_LIMIT

sealed class AskQuestionPage<ContentType : Any>(
    val page: @Composable () -> Unit,
    val canContinue: (ContentType) -> Boolean = { true },
) {
    val ordinal: Int
        get() = values().indexOf(this)

    object Start : AskQuestionPage<Nothing>(page = { StartPage() })
    object Title : AskQuestionPage<String>(
        page = { TitlePage() },
        canContinue = { title -> title.isNotBlank() && title.length <= TITLE_LENGTH_LIMIT },
    ) {
        internal const val TITLE_LENGTH_LIMIT = 150
    }
    object Details : AskQuestionPage<String>(
        page = { DetailsPage() },
        canContinue = { details -> details.length > MIN_DETAILS_LENGTH },
    ) {
        internal const val MIN_DETAILS_LENGTH = 20
    }
    object ExpandDetails : AskQuestionPage<String>(
        page = { ExpandDetailsPage() },
        canContinue = { details -> details.length > MIN_DETAILS_LENGTH },
    )
    object Tags : AskQuestionPage<Set<Tag>>(page = { TagsPage() })
    object DuplicateQuestion : AskQuestionPage<Boolean>(
        page = { DuplicateQuestionPage() },
        canContinue = { isChecked -> isChecked },
    )
    object Review : AskQuestionPage<Nothing>(page = { ReviewPage() })

    companion object {
        fun values(): List<AskQuestionPage<*>> = listOf(
            Start,
            Title,
            Details,
            ExpandDetails,
            Tags,
            DuplicateQuestion,
            Review,
        )

        fun getCurrentPage(page: Int): AskQuestionPage<*>? = values().getOrNull(page)
    }
}
