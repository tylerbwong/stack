package me.tylerbwong.stack.ui.questions

import android.content.Context
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class QuestionDataModel(
    internal val question: Question,
    internal val isDetail: Boolean = false
) : DynamicDataModel() {

    internal val questionTitle = question.title
    internal val answerCount = question.answerCount
    internal val questionBody = if (isDetail) question.bodyMarkdown else question.body
    internal val userImage = question.owner.profileImage
    internal val username = question.owner.displayName
    internal val reputation = question.owner.reputation
    internal val badgeCounts = question.owner.badgeCounts
    internal val shareLink = question.shareLink
    internal val questionId = question.questionId
    internal val owner = question.owner
    internal val tags = question.tags

    internal fun onProfilePictureClicked(context: Context) {
        ProfileActivity.startActivity(context, owner.userId)
    }

    override fun areItemsThemSame(
        other: DynamicDataModel
    ) = other is QuestionDataModel && other.questionId == questionId

    override fun areContentsTheSame(
        other: DynamicDataModel
    ) = other is QuestionDataModel && other.questionTitle == questionTitle &&
            other.answerCount == answerCount && other.questionBody == questionBody &&
            other.userImage == userImage && other.username == username &&
            other.reputation == reputation && other.badgeCounts == badgeCounts && other.tags == tags

    override fun getViewCreator() = ::QuestionHolder
}
