package me.tylerbwong.stack.ui.questions.detail

import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import androidx.core.text.set
import com.soywiz.klock.seconds
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionNoticeBinding
import me.tylerbwong.stack.ui.utils.formatElapsedTime

class QuestionNoticeHolder(
    parent: ViewGroup
) : DynamicViewBindingHolder<QuestionNoticeItem, QuestionNoticeBinding>(
    parent,
    QuestionNoticeBinding::inflate
) {
    override fun QuestionNoticeBinding.bind(item: QuestionNoticeItem) {
        val originalQuestionResult = item.closedDetails.originalQuestions.single()
        val spannableString = SpannableString(originalQuestionResult.title)
        spannableString[0, spannableString.length] = object : ClickableSpan() {
            override fun onClick(view: View) {
                QuestionDetailActivity.startActivity(
                    view.context,
                    originalQuestionResult.questionId
                )
            }

        }
        originalQuestion.text = spannableString
        originalQuestion.movementMethod = BetterLinkMovementMethod.getInstance()
        closedTimestamp.text = closedTimestamp.context.getString(
            R.string.closed,
            item.closedDate.seconds.millisecondsLong.formatElapsedTime(closedTimestamp.context)
        )
    }
}
