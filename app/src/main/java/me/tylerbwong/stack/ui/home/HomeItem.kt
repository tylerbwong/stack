package me.tylerbwong.stack.ui.home

import androidx.recyclerview.widget.DiffUtil
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.data.model.Tag

sealed class HomeItem
data class HeaderItem(val title: String, val subtitle: String? = null) : HomeItem()
data class QuestionItem(val question: Question) : HomeItem()
data class AnswerDraftItem(val draft: AnswerDraft) : HomeItem()
data class BasicSearchInputItem(
    val searchPayload: SearchPayload,
    val onPayloadReceived: (SearchPayload) -> Unit
) : HomeItem()
data class AdvancedSearchInputItem(
    val searchPayload: SearchPayload,
    val onPayloadReceived: (SearchPayload) -> Unit
) : HomeItem()
data class TagsItem(val tags: List<Tag>) : HomeItem()
data class SectionHeaderItem(val header: String) : HomeItem()

class HomeItemDiffCallback : DiffUtil.ItemCallback<HomeItem>() {

    @Suppress("ComplexMethod")
    override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem) =
        oldItem.javaClass == newItem.javaClass &&
                (oldItem is HeaderItem || oldItem is QuestionItem && newItem is QuestionItem &&
                        oldItem.question.questionId == newItem.question.questionId ||
                        oldItem is AnswerDraftItem && newItem is AnswerDraftItem &&
                        oldItem.draft.questionId == newItem.draft.questionId ||
                        oldItem is BasicSearchInputItem && newItem is BasicSearchInputItem ||
                        oldItem is AdvancedSearchInputItem && newItem is AdvancedSearchInputItem ||
                        oldItem is TagsItem && newItem is TagsItem ||
                        oldItem is SectionHeaderItem && newItem is SectionHeaderItem)

    @Suppress("ComplexMethod")
    override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem) = when {
        oldItem is HeaderItem && newItem is HeaderItem -> oldItem == newItem
        oldItem is QuestionItem && newItem is QuestionItem ->
            oldItem.question.title == newItem.question.title &&
                    oldItem.question.answerCount == newItem.question.answerCount &&
                    oldItem.question.owner == newItem.question.owner
        oldItem is AnswerDraftItem && newItem is AnswerDraftItem ->
            oldItem.draft.questionTitle == newItem.draft.questionTitle &&
                    oldItem.draft.formattedTimestamp == newItem.draft.formattedTimestamp &&
                    oldItem.draft.bodyMarkdown == newItem.draft.bodyMarkdown
        oldItem is BasicSearchInputItem && newItem is BasicSearchInputItem -> true
        oldItem is AdvancedSearchInputItem && newItem is AdvancedSearchInputItem ->
            oldItem.searchPayload == newItem.searchPayload
        oldItem is TagsItem && newItem is TagsItem ->
            oldItem.tags == newItem.tags
        oldItem is SectionHeaderItem && newItem is SectionHeaderItem ->
            oldItem.header == newItem.header
        else -> false
    }
}
