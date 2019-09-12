package me.tylerbwong.stack.ui.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.question_holder.*
import kotlinx.android.synthetic.main.user_view.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.*
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import java.text.DateFormat
import java.util.*

class QuestionHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflateWithoutAttaching(R.layout.question_holder)
) {
    override fun bind(data: Any) {
        (data as? QuestionDataModel)?.let { dataModel ->
            if (dataModel.isDetail) {
                questionBody.maxLines = Integer.MAX_VALUE
                questionBody.ellipsize = null
            }

            questionViewsCount.text = containerView.context.getString(R.string.asked_views, getViewsCount(dataModel.question.viewCount))

            dataModel.question.lastActivityDate?.let {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it * 1000
                val dateFormatted = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
                lastActivityDate.text = containerView.context.getString(R.string.last_active_date, dateFormatted)
                lastActivityDate.visibility = View.VISIBLE
            }

            questionTitle.text = dataModel.questionTitle.toHtml()
            answerCount.apply {
                if (dataModel.isDetail) {
                    visibility = View.GONE
                } else {
                    text = dataModel.answerCount.toString()
                    visibility = View.VISIBLE
                }
            }

            questionBody.apply {
                if (dataModel.isDetail) {
                    dataModel.questionBody?.let { body ->
                        setMarkdown(body)
                    }

                    setTextIsSelectable(true)
                    movementMethod = BetterLinkMovementMethod.getInstance()
                } else {
                    text = dataModel.questionBody?.toHtml()
                    setTextIsSelectable(false)
                }
            }

            creationDate.text = getAskFormattedDate(dataModel.question.creationDate)

            username.text = dataModel.username.toHtml()
            GlideApp.with(itemView)
                .load(dataModel.userImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.user_image_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(userImage)
            userImage.setThrottledOnClickListener { dataModel.onProfilePictureClicked(it.context) }
            badgeView.badgeCounts = dataModel.badgeCounts
            reputation.text = dataModel.reputation.toLong().format()

            tagsView.visibility = if (dataModel.isDetail) {
                tagsView.removeAllViews()
                dataModel.tags?.forEach {
                    val chip = Chip(tagsView.context).apply {
                        text = it
                        setThrottledOnClickListener { view ->
                            QuestionsActivity.startActivityForKey(view.context, TAGS, it)
                        }
                    }
                    tagsView.addView(chip)
                }
                View.VISIBLE
            } else {
                View.GONE
            }

            if (!dataModel.isDetail) {
                itemView.setOnLongClickListener {
                    val context = it.context
                    val contentManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    contentManager.setPrimaryClip(
                        ClipData.newPlainText(
                            "linkText",
                            dataModel.shareLink
                        )
                    )
                    Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                    true
                }

                itemView.setThrottledOnClickListener {
                    QuestionDetailActivity.startActivity(
                        it.context,
                        dataModel.questionId,
                        dataModel.questionTitle,
                        dataModel.questionBody,
                        dataModel.owner
                    )
                }
            }
        }
    }

    private fun getAskFormattedDate(creationDate: Long): String {
        val calendarStack = Calendar.getInstance()
        calendarStack.timeInMillis = creationDate * 1000

        val calendarCurrent = Calendar.getInstance()

        return when {
            calendarCurrent.get(Calendar.YEAR) > calendarStack.get(Calendar.YEAR) -> getDateCalculation(calendarStack.timeInMillis, CALC_YEAR)
            calendarCurrent.get(Calendar.MONTH) > calendarStack.get(Calendar.MONTH) -> getDateCalculation(calendarStack.timeInMillis, CALC_MONTH)
            calendarCurrent.get(Calendar.DAY_OF_MONTH) > calendarStack.get(Calendar.DAY_OF_MONTH) -> getDateCalculation(calendarStack.timeInMillis, CALC_DAY)
            calendarCurrent.get(Calendar.HOUR_OF_DAY) > calendarStack.get(Calendar.HOUR_OF_DAY) -> getDateCalculation(calendarStack.timeInMillis, CALC_HOUR)
            calendarCurrent.get(Calendar.MINUTE) - calendarStack.get(Calendar.MINUTE) > 5 -> getDateCalculation(calendarStack.timeInMillis, CALC_MINUTES)
            else -> containerView.context.getString(R.string.asked_min_ago)
        }
    }

    private fun getDateCalculation(daysBefore: Long, typeDate: Int): String {
        return when (typeDate) {
            CALC_YEAR -> containerView.context.getString(R.string.asked_years_ago, calendarCalculation(daysBefore, typeDate))
            CALC_MONTH -> containerView.context.getString(R.string.asked_months_ago, calendarCalculation(daysBefore, typeDate))
            CALC_DAY -> containerView.context.getString(R.string.asked_days_ago, calendarCalculation(daysBefore, typeDate))
            CALC_HOUR -> containerView.context.getString(R.string.asked_hours_ago, calendarCalculation(daysBefore, typeDate))
            else -> containerView.context.getString(R.string.asked_minutes_ago, calendarCalculation(daysBefore, typeDate))
        }
    }



}
