package me.tylerbwong.stack.ui.answers

import android.content.Context
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.utils.DynamicDataModel
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.toHtml

class AnswerDataModel(private val answer: Answer) : DynamicDataModel() {

    internal val answerId = answer.answerId
    internal val isAccepted = answer.isAccepted
    internal val voteCount = answer.upVoteCount - answer.downVoteCount
    internal val answerBody = answer.bodyMarkdown
    internal val userImage = answer.owner.profileImage
    internal val username = answer.owner.displayName.toHtml()
    internal val reputation = answer.owner.reputation.toLong().format()
    internal val badgeCounts = answer.owner.badgeCounts
    internal val creationDate = answer.creationDate

    internal fun onProfilePictureClicked(context: Context) {
        ProfileActivity.startActivity(context, answer.owner.userId)
    }

    override fun areItemsThemSame(
        other: DynamicDataModel
    ) = other is AnswerDataModel && other.answerId == answerId

    override fun areContentsTheSame(
        other: DynamicDataModel
    ) = other is AnswerDataModel && other.isAccepted == isAccepted &&
            other.voteCount == voteCount && other.answerBody == answerBody &&
            other.userImage == userImage && other.username == username &&
            other.reputation == reputation && other.badgeCounts == badgeCounts &&
            other.creationDate == creationDate

    override fun getViewCreator() = ::AnswerHolder
}
