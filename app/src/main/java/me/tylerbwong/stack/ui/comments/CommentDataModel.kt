package me.tylerbwong.stack.ui.comments

import android.content.Context
import me.tylerbwong.stack.data.model.Comment
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class CommentDataModel(comment: Comment) : DynamicDataModel() {

    internal val commentId = comment.commentId
    internal val bodyMarkdown = comment.bodyMarkdown
    internal val owner = comment.owner
    internal val username = owner.displayName
    internal val userImage = owner.profileImage
    internal val badgeCounts = owner.badgeCounts
    internal val reputation = owner.reputation

    internal fun onProfilePictureClicked(context: Context) {
        ProfileActivity.startActivity(context, owner.userId)
    }

    override fun areItemsThemSame(
        other: DynamicDataModel
    ) = other is CommentDataModel && other.commentId == commentId

    override fun areContentsTheSame(other: DynamicDataModel) = other is CommentDataModel &&
            other.bodyMarkdown == bodyMarkdown && other.owner == owner

    override fun getViewCreator() = ::CommentHolder
}
