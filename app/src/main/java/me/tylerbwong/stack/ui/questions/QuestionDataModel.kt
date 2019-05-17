package me.tylerbwong.stack.ui.questions

import android.content.Context
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.utils.DynamicDataModel

data class QuestionDataModel(
        internal val question: Question,
        internal val isDetail: Boolean = false
) : DynamicDataModel() {

    internal fun onProfilePictureClicked(context: Context) {
        ProfileActivity.startActivity(context, question.owner.userId)
    }

    override fun areItemsThemSame(
            other: DynamicDataModel
    ) = other is QuestionDataModel && other.question.questionId == question.questionId

    override fun areContentsTheSame(
            other: DynamicDataModel
    ) = other is QuestionDataModel && other == this

    override fun getViewCreator() = ::QuestionHolder
}
