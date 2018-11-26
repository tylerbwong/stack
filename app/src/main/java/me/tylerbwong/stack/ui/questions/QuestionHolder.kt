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

    private lateinit var question: Question

    override fun bind(data: Any) {
        (data as? QuestionDataModel)?.let {
            if (it.isDetail) {
                questionBody.maxLines = Integer.MAX_VALUE
                questionBody.ellipsize = null
            }

            this.question = it.question
            this.questionTitle.text = question.title.toHtml()

            if (it.isDetail) {
                question.bodyMarkdown?.let { body ->
                    this.questionBody.setMarkdown(body)
                }
            } else {
                this.questionBody.text = question.body?.toHtml()
            }

            this.username.text = question.owner.displayName.toHtml()
            GlideApp.with(itemView)
                    .load(question.owner.profileImage)
                    .placeholder(R.drawable.user_image_placeholder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage)
            this.badgeView.badgeCounts = question.owner.badgeCounts
            this.reputation.text = question.owner.reputation.toLong().format()

            itemView.setOnLongClickListener { view ->
                val context = view.context
                val contentManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                contentManager.primaryClip = ClipData.newPlainText("linkText", question.shareLink)
                Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                true
            }

            itemView.setOnClickListener { view ->
                QuestionDetailActivity.startActivity(
                        view.context,
                        question.questionId,
                        question.title,
                        question.body,
                        question.owner
                )
            }
        }
    }
}