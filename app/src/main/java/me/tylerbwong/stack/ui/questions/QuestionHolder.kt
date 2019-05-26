package me.tylerbwong.stack.ui.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.ui.owners.BadgeView
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.GlideApp
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching
import me.tylerbwong.stack.ui.utils.setMarkdown
import me.tylerbwong.stack.ui.utils.toHtml

class QuestionHolder(parent: ViewGroup) : DynamicViewHolder(
        parent.inflateWithoutAttaching(R.layout.question_holder)
) {
    private val questionTitle: TextView = ViewCompat.requireViewById(itemView, R.id.questionTitle)
    private val questionBody: TextView = ViewCompat.requireViewById(itemView, R.id.questionBody)
    private val userImage: ImageView = ViewCompat.requireViewById(itemView, R.id.userImage)
    private val username: TextView = ViewCompat.requireViewById(itemView, R.id.username)
    private val reputation: TextView = ViewCompat.requireViewById(itemView, R.id.reputation)
    private val badgeView: BadgeView = ViewCompat.requireViewById(itemView, R.id.badgeView)
    private val tagsView: ChipGroup = ViewCompat.requireViewById(itemView, R.id.tagsView)

    private lateinit var question: Question

    override fun bind(data: Any) {
        (data as? QuestionDataModel)?.let { dataModel ->
            if (dataModel.isDetail) {
                questionBody.maxLines = Integer.MAX_VALUE
                questionBody.ellipsize = null
            }

            question = dataModel.question
            questionTitle.text = dataModel.questionTitle.toHtml()

            if (dataModel.isDetail) {
                dataModel.questionBody?.let { body ->
                    questionBody.setMarkdown(body)
                }
            } else {
                questionBody.text = dataModel.questionBody?.toHtml()
            }

            username.text = dataModel.username.toHtml()
            GlideApp.with(itemView)
                    .load(dataModel.userImage)
                    .placeholder(R.drawable.user_image_placeholder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage)
            userImage.setOnClickListener { dataModel.onProfilePictureClicked(it.context) }
            badgeView.badgeCounts = dataModel.badgeCounts
            reputation.text = dataModel.reputation.toLong().format()

            tagsView.removeAllViews()

            dataModel.tags.forEach {
                val chip = Chip(tagsView.context).apply {
                    text = it
                }
                tagsView.addView(chip)
            }

            if (!dataModel.isDetail) {
                itemView.setOnLongClickListener {
                    val context = it.context
                    val contentManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    contentManager.primaryClip = ClipData.newPlainText("linkText", dataModel.shareLink)
                    Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                    true
                }

                itemView.setOnClickListener {
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
