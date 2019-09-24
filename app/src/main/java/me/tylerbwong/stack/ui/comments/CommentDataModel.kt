package me.tylerbwong.stack.ui.comments

import me.tylerbwong.stack.data.model.Comment
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class CommentDataModel(comment: Comment) : DynamicDataModel() {

    internal val commentId = comment.commentId
    internal val bodyMarkdown = comment.bodyMarkdown
    internal val owner = comment.owner

    override fun areItemsThemSame(
        other: DynamicDataModel
    ) = other is CommentDataModel && other.commentId == commentId

    override fun areContentsTheSame(other: DynamicDataModel) = other is CommentDataModel &&
            other.bodyMarkdown == bodyMarkdown && other.owner == owner

    override fun getViewCreator() = ::CommentHolder
}
