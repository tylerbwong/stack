package me.tylerbwong.stack.ui.home

import androidx.recyclerview.widget.DiffUtil
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.data.model.Tag
import me.tylerbwong.stack.ui.HeaderViewHolder
import me.tylerbwong.stack.ui.adapter.DelegatedItem
import me.tylerbwong.stack.ui.adapter.ViewHolderProvider
import me.tylerbwong.stack.ui.drafts.AnswerDraftHolder
import me.tylerbwong.stack.ui.questions.QuestionViewHolder
import me.tylerbwong.stack.ui.search.SearchHistoryItemHolder
import me.tylerbwong.stack.ui.search.SearchInputHolder
import me.tylerbwong.stack.ui.search.filters.FilterInputHolder
import me.tylerbwong.stack.ui.search.tags.TagsHolder

sealed class HomeItem(viewHolderProvider: ViewHolderProvider) : DelegatedItem(viewHolderProvider)
data class HeaderItem(val title: String, val subtitle: String? = null) : HomeItem(::HeaderViewHolder)
data class QuestionItem(val question: Question) : HomeItem(::QuestionViewHolder)
data class AnswerDraftItem(val draft: AnswerDraft) : HomeItem(::AnswerDraftHolder)
data class SearchInputItem(
    val searchPayload: SearchPayload,
    val onPayloadReceived: (SearchPayload) -> Unit
) : HomeItem(::SearchInputHolder)
data class FilterInputItem(
    val searchPayload: SearchPayload,
    val onPayloadReceived: (SearchPayload) -> Unit
) : HomeItem(::FilterInputHolder)
data class TagsItem(val tags: List<Tag>) : HomeItem(::TagsHolder)
data class SectionHeaderItem(val header: String) : HomeItem(::SectionHeaderHolder)
data class SearchHistoryItem(
    val searchPayload: SearchPayload,
    val onPayloadReceived: (SearchPayload) -> Unit
) : HomeItem(::SearchHistoryItemHolder)

object HomeItemDiffCallback : DiffUtil.ItemCallback<DelegatedItem>() {
    @Suppress("ComplexMethod")
    override fun areItemsTheSame(oldItem: DelegatedItem, newItem: DelegatedItem) =
        oldItem.javaClass == newItem.javaClass &&
                (oldItem is HeaderItem || oldItem is QuestionItem && newItem is QuestionItem &&
                        oldItem.question.questionId == newItem.question.questionId ||
                        oldItem is AnswerDraftItem && newItem is AnswerDraftItem &&
                        oldItem.draft.questionId == newItem.draft.questionId ||
                        oldItem is SearchInputItem && newItem is SearchInputItem ||
                        oldItem is FilterInputItem && newItem is FilterInputItem ||
                        oldItem is TagsItem && newItem is TagsItem ||
                        oldItem is SectionHeaderItem && newItem is SectionHeaderItem ||
                        oldItem is SearchHistoryItem && newItem is SearchHistoryItem)

    @Suppress("ComplexMethod")
    override fun areContentsTheSame(oldItem: DelegatedItem, newItem: DelegatedItem) = when {
        oldItem is HeaderItem && newItem is HeaderItem ->
            oldItem.title == newItem.title && oldItem.subtitle == newItem.subtitle
        oldItem is QuestionItem && newItem is QuestionItem ->
            oldItem.question.title == newItem.question.title &&
                    oldItem.question.answerCount == newItem.question.answerCount &&
                    oldItem.question.owner == newItem.question.owner
        oldItem is AnswerDraftItem && newItem is AnswerDraftItem ->
            oldItem.draft.questionTitle == newItem.draft.questionTitle &&
                    oldItem.draft.formattedTimestamp == newItem.draft.formattedTimestamp &&
                    oldItem.draft.bodyMarkdown == newItem.draft.bodyMarkdown
        oldItem is SearchInputItem && newItem is SearchInputItem ->
            oldItem.searchPayload == newItem.searchPayload
        oldItem is FilterInputItem && newItem is FilterInputItem ->
            oldItem.searchPayload == newItem.searchPayload
        oldItem is TagsItem && newItem is TagsItem ->
            oldItem.tags == newItem.tags
        oldItem is SectionHeaderItem && newItem is SectionHeaderItem ->
            oldItem.header == newItem.header
        oldItem is SearchHistoryItem && newItem is SearchHistoryItem ->
            oldItem.searchPayload == newItem.searchPayload
        else -> false
    }
}
