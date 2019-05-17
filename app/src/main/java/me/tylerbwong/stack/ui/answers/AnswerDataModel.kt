package me.tylerbwong.stack.ui.answers

import android.content.Context
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.utils.DynamicDataModel

data class AnswerDataModel(internal val answer: Answer) : DynamicDataModel() {

    internal fun onProfilePictureClicked(context: Context) {
        ProfileActivity.startActivity(context, answer.owner.userId)
    }

    override fun areItemsThemSame(
            other: DynamicDataModel
    ) = other is AnswerDataModel && other.answer.answerId == answer.answerId

    override fun areContentsTheSame(
            other: DynamicDataModel
    ) = other is AnswerDataModel && other == this

    override fun getViewCreator() = ::AnswerHolder
}
