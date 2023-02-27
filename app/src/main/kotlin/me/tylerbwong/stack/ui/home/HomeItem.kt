package me.tylerbwong.stack.ui.home

import androidx.recyclerview.widget.DiffUtil
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.adapter.ViewHolderProvider
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Tag
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.data.model.QuestionDraft
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.ui.HeaderHolder
import me.tylerbwong.stack.ui.drafts.AnswerDraftHolder
import me.tylerbwong.stack.ui.drafts.QuestionDraftHolder
import me.tylerbwong.stack.ui.home.tags.TagHeaderHolder
import me.tylerbwong.stack.ui.questions.QuestionCardHolder
import me.tylerbwong.stack.ui.questions.QuestionCarouselHolder
import me.tylerbwong.stack.ui.questions.QuestionHolder
import me.tylerbwong.stack.ui.search.SearchHistoryItemHolder
import me.tylerbwong.stack.ui.search.SearchInputHolder
import me.tylerbwong.stack.ui.search.filters.FilterInputHolder
import me.tylerbwong.stack.ui.search.tags.TagsHolder

sealed class HomeItem(viewHolderProvider: ViewHolderProvider) : DynamicItem(viewHolderProvider)
data class HeaderItem(val title: String, val subtitle: String? = null) : HomeItem(::HeaderHolder)
data class QuestionItem(val question: Question) : HomeItem(::QuestionHolder)
data class AnswerDraftItem(val draft: AnswerDraft) : HomeItem(::AnswerDraftHolder)
data class QuestionDraftItem(val draft: QuestionDraft) : HomeItem(::QuestionDraftHolder)
data class SearchInputItem(
    val searchPayload: SearchPayload,
    val onPayloadReceived: (SearchPayload) -> Unit
) : HomeItem(::SearchInputHolder)

data class FilterInputItem(
    val searchPayload: SearchPayload,
    val onPayloadReceived: (SearchPayload) -> Unit
) : HomeItem(::FilterInputHolder)

data class TagsItem(val tags: List<List<Tag>>) : HomeItem(::TagsHolder)
data class SectionHeaderItem(val header: String) : HomeItem(::SectionHeaderHolder)
data class SearchHistoryItem(
    val searchPayload: SearchPayload,
    val onPayloadReceived: (SearchPayload) -> Unit
) : HomeItem(::SearchHistoryItemHolder)

data class TagHeaderItem(val tagName: String) : HomeItem(::TagHeaderHolder)
data class QuestionCardItem(val question: Question) : HomeItem(::QuestionCardHolder)
data class QuestionCarouselItem(
    val questions: List<QuestionCardItem>
) : HomeItem(::QuestionCarouselHolder)

object HomeItemDiffCallback : DiffUtil.ItemCallback<DynamicItem>() {
    @Suppress("ComplexMethod")
    override fun areItemsTheSame(oldItem: DynamicItem, newItem: DynamicItem) =
        oldItem.javaClass == newItem.javaClass &&
                (oldItem is HeaderItem || oldItem is QuestionItem && newItem is QuestionItem &&
                        oldItem.question.questionId == newItem.question.questionId ||
                        oldItem is AnswerDraftItem && newItem is AnswerDraftItem &&
                        oldItem.draft.questionId == newItem.draft.questionId ||
                        oldItem is QuestionDraftItem && newItem is QuestionDraftItem &&
                        oldItem.draft.id == newItem.draft.id ||
                        oldItem is SearchInputItem && newItem is SearchInputItem ||
                        oldItem is FilterInputItem && newItem is FilterInputItem ||
                        oldItem is TagsItem && newItem is TagsItem ||
                        oldItem is SectionHeaderItem && newItem is SectionHeaderItem ||
                        oldItem is SearchHistoryItem && newItem is SearchHistoryItem ||
                        oldItem is TagHeaderItem && newItem is TagHeaderItem ||
                        oldItem is QuestionCardItem && newItem is QuestionCardItem ||
                        oldItem is QuestionCarouselItem && newItem is QuestionCarouselItem)

    @Suppress("ComplexMethod")
    override fun areContentsTheSame(oldItem: DynamicItem, newItem: DynamicItem) = when {
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
        oldItem is QuestionDraftItem && newItem is QuestionDraftItem ->
            oldItem.draft.title == newItem.draft.title &&
                    oldItem.draft.formattedTimestamp == newItem.draft.formattedTimestamp &&
                    oldItem.draft.body == newItem.draft.body
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
        oldItem is TagHeaderItem && newItem is TagHeaderItem ->
            oldItem.tagName == newItem.tagName
        oldItem is QuestionCardItem && newItem is QuestionCardItem ->
            oldItem.question.title == newItem.question.title &&
                    oldItem.question.answerCount == newItem.question.answerCount &&
                    oldItem.question.owner == newItem.question.owner
        oldItem is QuestionCarouselItem && newItem is QuestionCarouselItem ->
            oldItem.questions == newItem.questions
        else -> false
    }
}
