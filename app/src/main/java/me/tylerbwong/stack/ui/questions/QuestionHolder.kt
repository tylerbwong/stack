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
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.questions.tags.SingleTagQuestionsActivity
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.GlideApp
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class QuestionHolder(parent: ViewGroup) : DynamicViewHolder(
        parent.inflateWithoutAttaching(R.layout.question_holder)
) {
    override fun bind(data: Any) {
        (data as? QuestionDataModel)?.let { dataModel ->
            if (dataModel.isDetail) {
                questionBody.maxLines = Integer.MAX_VALUE
                questionBody.ellipsize = null
            }

            questionTitle.text = dataModel.questionTitle.toHtml()
            answerCount.text = dataModel.answerCount.toString()

            if (dataModel.isDetail) {
                dataModel.questionBody?.let { body ->
                    questionBody.setMarkdown(body)
                }
                answerCount.visibility = View.GONE
            } else {
                questionBody.text = dataModel.questionBody?.toHtml()
                answerCount.visibility = View.VISIBLE
            }

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
                            SingleTagQuestionsActivity.startActivity(view.context, it)
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
                    val contentManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    contentManager.setPrimaryClip(ClipData.newPlainText("linkText", dataModel.shareLink))
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
}
