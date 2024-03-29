package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.runtime.Composable
import me.tylerbwong.stack.api.model.Tag
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Details.MIN_DETAILS_LENGTH
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Title.TITLE_LENGTH_MAX
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Title.TITLE_LENGTH_MIN

sealed class AskQuestionPage<ContentType : Any>(
    val page: @Composable (isDetailedQuestionRequired: Boolean) -> Unit,
    val canContinue: (ContentType, isDetailedQuestionRequired: Boolean) -> Boolean = { _, _ -> true },
) {
    val ordinal: Int
        get() = values().indexOf(this)

    object Start : AskQuestionPage<Nothing>(page = { StartPage() })
    object Title : AskQuestionPage<String>(
        page = { TitlePage() },
        canContinue = { title, _ ->
            title.isNotBlank() && title.length in TITLE_LENGTH_MIN..TITLE_LENGTH_MAX
        },
    ) {
        internal const val TITLE_LENGTH_MIN = 15
        internal const val TITLE_LENGTH_MAX = 150
    }

    object Details : AskQuestionPage<String>(
        page = { DetailsPage(it) },
        canContinue = { details, _ -> details.isNotBlank() && details.length > MIN_DETAILS_LENGTH },
    ) {
        internal const val MIN_DETAILS_LENGTH = 20
    }

    object ExpandDetails : AskQuestionPage<String>(
        page = { ExpandDetailsPage(it) },
        canContinue = { details, isDetailedQuestionRequired ->
            !isDetailedQuestionRequired || details.isNotBlank() && details.length > MIN_DETAILS_LENGTH
        },
    )

    object Tags : AskQuestionPage<Set<Tag>>(
        page = { TagsPage() },
        canContinue = { tags, _ -> tags.isNotEmpty() }
    ) {
        internal const val MAX_NUM_TAGS = 5
    }

    object DuplicateQuestion : AskQuestionPage<Boolean>(
        page = { DuplicateQuestionPage() },
        canContinue = { isChecked, _ -> isChecked },
    )

    object Review : AskQuestionPage<Nothing>(page = { ReviewPage() })
    object Success : AskQuestionPage<Nothing>(page = { SuccessPage() })

    companion object {
        fun values(): List<AskQuestionPage<*>> = listOf(
            Start,
            Title,
            Details,
            ExpandDetails,
            Tags,
            DuplicateQuestion,
            Review,
            Success,
        )

        fun getCurrentPage(page: Int): AskQuestionPage<*>? = values().getOrNull(page)
    }
}
