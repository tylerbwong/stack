package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.runtime.Composable
import me.tylerbwong.stack.api.model.Tag

sealed class AskQuestionPage<ContentType : Any>(
    val page: @Composable (isDetailedQuestionRequired: Boolean) -> Unit,
    val canContinue: (ContentType, isDetailedQuestionRequired: Boolean) -> Boolean = { _, _ -> true },
) {
    val ordinal: Int
        get() = values().indexOf(this)

    data object Start : AskQuestionPage<Nothing>(page = { StartPage() })
    data object Title : AskQuestionPage<String>(
        page = { TitlePage() },
        canContinue = { title, _ ->
            title.isNotBlank() && title.length in TITLE_LENGTH_MIN..TITLE_LENGTH_MAX
        },
    )

    data object Details : AskQuestionPage<String>(
        page = { DetailsPage(it) },
        canContinue = { details, _ -> details.isNotBlank() && details.length > MIN_DETAILS_LENGTH },
    )

    data object ExpandDetails : AskQuestionPage<String>(
        page = { ExpandDetailsPage(it) },
        canContinue = { details, isDetailedQuestionRequired ->
            !isDetailedQuestionRequired || details.isNotBlank() && details.length > MIN_DETAILS_LENGTH
        },
    )

    data object Tags : AskQuestionPage<Set<Tag>>(
        page = { TagsPage() },
        canContinue = { tags, _ -> tags.isNotEmpty() }
    )

    data object DuplicateQuestion : AskQuestionPage<Boolean>(
        page = { DuplicateQuestionPage() },
        canContinue = { isChecked, _ -> isChecked },
    )

    data object Review : AskQuestionPage<Nothing>(page = { ReviewPage() })
    data object Success : AskQuestionPage<Nothing>(page = { SuccessPage() })

    companion object {

        internal const val TITLE_LENGTH_MIN = 15
        internal const val TITLE_LENGTH_MAX = 150
        internal const val MIN_DETAILS_LENGTH = 20
        internal const val MAX_NUM_TAGS = 5

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
