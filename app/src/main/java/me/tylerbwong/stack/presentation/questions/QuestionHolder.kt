package me.tylerbwong.stack.presentation.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.presentation.owners.BadgeView
import me.tylerbwong.stack.presentation.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.presentation.utils.GlideApp
import me.tylerbwong.stack.presentation.utils.format
import me.tylerbwong.stack.presentation.utils.toHtml

class QuestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val questionTitle: TextView = ViewCompat.requireViewById(itemView, R.id.questionTitle)
    private val questionBody: TextView = ViewCompat.requireViewById(itemView, R.id.questionBody)
    private val userImage: ImageView = ViewCompat.requireViewById(itemView, R.id.userImage)
    private val username: TextView = ViewCompat.requireViewById(itemView, R.id.username)
    private val reputation: TextView = ViewCompat.requireViewById(itemView, R.id.reputation)
    private val badgeView: BadgeView = ViewCompat.requireViewById(itemView, R.id.badgeView)

    private lateinit var question: Question

    fun bind(question: Question) {
        this.question = question
        this.questionTitle.text = question.title.toHtml()
        this.questionBody.text = question.body?.toHtml()

        this.username.text = question.owner.displayName.toHtml()
        GlideApp.with(itemView)
                .load(question.owner.profileImage)
                .placeholder(R.drawable.user_image_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(userImage)
        this.badgeView.badgeCounts = question.owner.badgeCounts
        this.reputation.text = question.owner.reputation.toLong().format()

        itemView.setOnLongClickListener {
            val contentManager = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            contentManager.primaryClip = ClipData.newPlainText("linkText", question.shareLink)
            Toast.makeText(it.context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
            true
        }

        itemView.setOnClickListener {
            QuestionDetailActivity.startActivity(
                    it.context,
                    question.questionId,
                    question.title,
                    question.body,
                    question.owner
            )
        }
    }
}
