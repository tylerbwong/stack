package me.tylerbwong.stack.ui.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.question_holder.*
import kotlinx.android.synthetic.main.user_view.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
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
    @Suppress("ComplexMethod", "LongMethod")
    override fun bind(data: Any) {
        (data as? QuestionDataModel)?.let { dataModel ->
            if (dataModel.isDetail) {
                rootQuestionView.elevation = 0f
                questionBody.maxLines = Integer.MAX_VALUE
                questionBody.ellipsize = null
            }

            questionTitle.text = dataModel.questionTitle.toHtml()
            lastEditor.apply {
                isVisible = dataModel.isDetail && dataModel.lastEditorName != null
                if (dataModel.isDetail) {
                    text = context.getString(R.string.last_edited_by, dataModel.lastEditorName)
                }
            }
            answerCount.apply {
                isVisible = !dataModel.isDetail
                if (!dataModel.isDetail) {
                    text = dataModel.answerCount.toString()
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

            tagsView.isVisible = dataModel.isDetail
            if (dataModel.isDetail) {
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
                    QuestionDetailActivity.startActivity(it.context, dataModel.questionId)
                }
            }
        }
    }
}
