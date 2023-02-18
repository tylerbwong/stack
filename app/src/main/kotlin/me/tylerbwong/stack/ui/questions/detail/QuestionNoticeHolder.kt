package me.tylerbwong.stack.ui.questions.detail

import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import androidx.core.view.isVisible
import com.soywiz.klock.seconds
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.ClosedDetails
import me.tylerbwong.stack.databinding.QuestionNoticeBinding
import me.tylerbwong.stack.ui.utils.formatElapsedTime

class QuestionNoticeHolder(
    parent: ViewGroup
) : DynamicViewBindingHolder<QuestionNoticeItem, QuestionNoticeBinding>(
    parent,
    QuestionNoticeBinding::inflate
) {
    override fun QuestionNoticeBinding.bind(item: QuestionNoticeItem) {
        closedReason.setText(
            when (item.closedDetails.closedReason) {
                ClosedDetails.ClosedReason.DUPLICATE -> R.string.duplicate_question_notice
                ClosedDetails.ClosedReason.NOT_SUITABLE -> R.string.not_suitable_notice
                ClosedDetails.ClosedReason.NEEDS_DETAILS -> R.string.needs_details_notice
                ClosedDetails.ClosedReason.NEEDS_FOCUS -> R.string.needs_focus_notice
                ClosedDetails.ClosedReason.OPINION_BASED -> R.string.opinion_based_notice
                ClosedDetails.ClosedReason.UNKNOWN -> R.string.unknown_notice
            }
        )
        originalQuestions.isVisible = item.closedDetails.originalQuestions.isNotEmpty()
        originalQuestions.text = buildSpannedString {
            var startIndex = 0
            item.closedDetails.originalQuestions.forEachIndexed { index, originalQuestion ->
                append(originalQuestion.title)
                this[startIndex, startIndex + originalQuestion.title.length] =
                    object : ClickableSpan() {
                        override fun onClick(view: View) {
                            QuestionDetailActivity.startActivity(
                                view.context,
                                originalQuestion.questionId
                            )
                        }
                    }
                startIndex += originalQuestion.title.length
                if (index != item.closedDetails.originalQuestions.lastIndex) {
                    appendLine()
                    appendLine()
                    startIndex++
                }
            }
        }
        originalQuestions.movementMethod = BetterLinkMovementMethod.getInstance()
        closedTimestamp.text = closedTimestamp.context.getString(
            R.string.closed,
            item.closedDate.seconds.millisecondsLong.formatElapsedTime(closedTimestamp.context)
        )
    }
}
