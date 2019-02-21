package me.tylerbwong.stack.ui.answers

import android.content.Context
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class AnswerDataModel(internal val answer: Answer) : DynamicDataModel() {

    internal fun onProfilePictureClicked(context: Context) {
        ProfileActivity.startActivity(context, answer.owner.userId)
    }

    override fun areItemsThemSame(
            other: DynamicDataModel
    ) = other is AnswerDataModel

    override fun areContentsTheSame(
            other: DynamicDataModel
    ) = other is AnswerDataModel && answer.answerId == other.answer.answerId

    override fun getViewCreator() = ::AnswerHolder
}
