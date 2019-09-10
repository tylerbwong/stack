package me.tylerbwong.stack.ui.answers

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.answer_holder.*
import kotlinx.android.synthetic.main.user_view.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.GlideApp
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import java.util.*
import java.util.concurrent.TimeUnit

class AnswerHolder(parent: ViewGroup) : DynamicViewHolder(
        parent.inflateWithoutAttaching(R.layout.answer_holder)
) {
    override fun bind(data: Any) {
        (data as? AnswerDataModel)?.let { dataModel ->
            val voteCount = dataModel.voteCount
            votes.text = itemView.context.resources.getQuantityString(R.plurals.votes, voteCount, voteCount)
            acceptedAnswerCheck.visibility = if (dataModel.isAccepted) View.VISIBLE else View.GONE

            answerBody.apply {
                setMarkdown(dataModel.answerBody)
                setTextIsSelectable(true)
                movementMethod = BetterLinkMovementMethod.getInstance()
            }

            creationDate.text = getAnswerDateFormatted(data.creationDate)

            username.text = dataModel.username
            GlideApp.with(itemView)
                    .load(dataModel.userImage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.user_image_placeholder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage)
            userImage.setThrottledOnClickListener { dataModel.onProfilePictureClicked(it.context) }
            reputation.text = dataModel.reputation
            badgeView.badgeCounts = dataModel.badgeCounts
        }
    }

    private fun getAnswerDateFormatted(creationDate: Long): String {
        val calendarStackAnswer = Calendar.getInstance()
        calendarStackAnswer.timeInMillis = creationDate * 1000

        val calendarCurrent = Calendar.getInstance()

        val timeCalculation = calendarCurrent.timeInMillis - calendarStackAnswer.timeInMillis

        return when {
            // less than
            TimeUnit.MILLISECONDS.toMinutes(timeCalculation) <= 5 -> containerView.context.getString(R.string.answered_min_ago)
            // x minutes ago
            TimeUnit.MILLISECONDS.toMinutes(timeCalculation) in 6..59 -> containerView.context.getString(R.string.answered_def_min_ago, TimeUnit.MILLISECONDS.toMinutes(timeCalculation).toInt())
            // hour ago
            TimeUnit.MILLISECONDS.toHours(timeCalculation).toInt() == 1 -> containerView.context.getString(R.string.answered_hr_ago)
            // x hours ago
            TimeUnit.MILLISECONDS.toHours(timeCalculation) in 2..23 -> containerView.context.getString(R.string.answered_hrs_ago, TimeUnit.MILLISECONDS.toHours(timeCalculation).toInt())
            // day ago
            TimeUnit.MILLISECONDS.toDays(timeCalculation).toInt() == 1 -> containerView.context.getString(R.string.answered_yesterday)
            // x days ago
            TimeUnit.MILLISECONDS.toDays(timeCalculation) in 2..29 -> containerView.context.getString(R.string.answered_days_ago, TimeUnit.MILLISECONDS.toDays(timeCalculation).toInt())
            // month ago
            TimeUnit.MILLISECONDS.toDays(timeCalculation) in 30..59 -> containerView.context.getString(R.string.answered_month_ago)
            // x months ago
            TimeUnit.MILLISECONDS.toDays(timeCalculation) in 60..364 -> containerView.context.getString(R.string.answered_months_ago, monthCalculation(calendarStackAnswer.timeInMillis))
            // x years ago
            else -> containerView.context.getString(R.string.answered_years_ago, yearCalculation(calendarStackAnswer.timeInMillis))
        }
    }

    private fun monthCalculation(daysBefore: Long): Int {
        val calendarAux = Calendar.getInstance()
        calendarAux.timeInMillis = daysBefore

        val month = calendarAux.get(Calendar.MONTH)

        return Calendar.getInstance().get(Calendar.MONTH) - month
    }

    private fun yearCalculation(daysBefore: Long): Int {
        val calendarAux = Calendar.getInstance()
        calendarAux.timeInMillis = daysBefore

        val year = calendarAux.get(Calendar.YEAR)

        return Calendar.getInstance().get(Calendar.YEAR) - year
    }
}
