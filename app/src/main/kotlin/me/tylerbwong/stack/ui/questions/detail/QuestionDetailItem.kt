package me.tylerbwong.stack.ui.questions.detail

import android.text.Spanned
import androidx.recyclerview.widget.DiffUtil
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.adapter.ViewHolderProvider
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.markdown.Renderer
import org.commonmark.node.Node

sealed class QuestionDetailItem(viewHolderProvider: ViewHolderProvider) : DynamicItem(viewHolderProvider)
abstract class BaseMarkdownItem(
    viewHolderProvider: ViewHolderProvider,
    internal var renderedMarkdown: Spanned? = null,
) : QuestionDetailItem(viewHolderProvider) {
    abstract fun render(renderer: Renderer): Spanned
}
data class QuestionTitleItem(internal val title: String) : QuestionDetailItem(::QuestionTitleHolder)
data class FooterItem(
    internal val entityId: Int,
    internal val creationDate: Long,
    internal val lastEditor: User?,
    internal val commentCount: Int?,
    internal val owner: User,
) : QuestionDetailItem(::FooterHolder)
data class QuestionTagsItem(internal val tags: List<String>) : QuestionDetailItem(::QuestionTagsHolder)
data class MarkdownItem(internal val node: Node) : BaseMarkdownItem(::MarkdownHolder) {
    override fun render(
        renderer: Renderer
    ): Spanned = this.renderedMarkdown ?: renderer.render(node).also { this.renderedMarkdown = it }
}
data class FencedCodeBlockItem(internal val node: Node) : BaseMarkdownItem(::FencedCodeBlockHolder) {
    override fun render(
        renderer: Renderer
    ): Spanned = this.renderedMarkdown ?: renderer.render(node).also { this.renderedMarkdown = it }
}
data class QuestionActionItem(
    internal val handler: QuestionDetailActionHandler,
    internal val question: Question
) : QuestionDetailItem(::QuestionDetailActionHolder)
data class AnswerHeaderItem(internal val answerCount: Int) : QuestionDetailItem(::AnswerHeaderViewHolder)
data class AnswerVotesHeaderItem(
    internal val id: Int,
    internal val isAccepted: Boolean,
    internal val upVoteCount: Int,
    internal val downVoteCount: Int
) : QuestionDetailItem(::AnswerVotesHeaderHolder)

object QuestionDetailItemCallback : DiffUtil.ItemCallback<DynamicItem>() {

    @Suppress("ComplexMethod") // TODO Delegate to each item
    override fun areItemsTheSame(oldItem: DynamicItem, newItem: DynamicItem) = when {
        oldItem is QuestionTitleItem && newItem is QuestionTitleItem ->
            oldItem.title == newItem.title
        oldItem is FooterItem && newItem is FooterItem ->
            oldItem.entityId == newItem.entityId
        oldItem is QuestionTagsItem && newItem is QuestionTagsItem ->
            oldItem.tags == newItem.tags
        oldItem is MarkdownItem && newItem is MarkdownItem ->
            oldItem.node.toString() == newItem.node.toString()
        oldItem is FencedCodeBlockItem && newItem is FencedCodeBlockItem ->
            oldItem.node.toString() == newItem.node.toString()
        oldItem is QuestionActionItem && newItem is QuestionActionItem -> true
        oldItem is AnswerHeaderItem && newItem is AnswerHeaderItem -> true
        oldItem is AnswerVotesHeaderItem && newItem is AnswerVotesHeaderItem ->
            oldItem.id == newItem.id
        else -> false
    }

    @Suppress("ComplexMethod") // TODO Delegate to each item
    override fun areContentsTheSame(oldItem: DynamicItem, newItem: DynamicItem) = when {
        oldItem is QuestionTitleItem && newItem is QuestionTitleItem ->
            oldItem.title == newItem.title
        oldItem is FooterItem && newItem is FooterItem ->
            oldItem.entityId == newItem.entityId && oldItem.creationDate == newItem.creationDate &&
                    oldItem.commentCount == newItem.commentCount &&
                    oldItem.lastEditor == newItem.lastEditor &&
                    oldItem.owner == newItem.owner
        oldItem is QuestionTagsItem && newItem is QuestionTagsItem ->
            oldItem.tags == newItem.tags
        oldItem is MarkdownItem && newItem is MarkdownItem ->
            oldItem.node.toString() == newItem.node.toString()
        oldItem is FencedCodeBlockItem && newItem is FencedCodeBlockItem ->
            oldItem.node.toString() == newItem.node.toString()
        oldItem is QuestionActionItem && newItem is QuestionActionItem ->
            oldItem.question.isDownVoted == newItem.question.isDownVoted &&
                    oldItem.question.isBookmarked == newItem.question.isBookmarked &&
                    oldItem.question.isUpVoted == newItem.question.isUpVoted &&
                    oldItem.question.downVoteCount == newItem.question.downVoteCount &&
                    oldItem.question.bookmarkCount == newItem.question.bookmarkCount &&
                    oldItem.question.upVoteCount == newItem.question.upVoteCount
        oldItem is AnswerHeaderItem && newItem is AnswerHeaderItem ->
            oldItem.answerCount == newItem.answerCount
        oldItem is AnswerVotesHeaderItem && newItem is AnswerVotesHeaderItem ->
            oldItem.id == newItem.id && oldItem.isAccepted == newItem.isAccepted &&
                    oldItem.upVoteCount == newItem.upVoteCount &&
                    oldItem.downVoteCount == newItem.downVoteCount
        else -> false
    }
}
