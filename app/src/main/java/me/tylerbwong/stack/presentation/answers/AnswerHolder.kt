package me.tylerbwong.stack.presentation.answers

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.presentation.owners.BadgeView
import me.tylerbwong.stack.presentation.utils.GlideApp
import me.tylerbwong.stack.presentation.utils.format
import me.tylerbwong.stack.presentation.utils.setMarkdown
import me.tylerbwong.stack.presentation.utils.toHtml

class AnswerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val acceptedAnswerCheck: ImageView = ViewCompat.requireViewById(itemView, R.id.acceptedAnswerCheck)
    private val votes: TextView = ViewCompat.requireViewById(itemView, R.id.votes)
    private val answerBody: TextView = ViewCompat.requireViewById(itemView, R.id.answerBody)
    private val userImage: ImageView = ViewCompat.requireViewById(itemView, R.id.userImage)
    private val username: TextView = ViewCompat.requireViewById(itemView, R.id.username)
    private val reputation: TextView = ViewCompat.requireViewById(itemView, R.id.reputation)
    private val badgeView: BadgeView = ViewCompat.requireViewById(itemView, R.id.badgeView)

    fun bind(answer: Answer) {
        val voteCount = answer.upVoteCount - answer.downVoteCount
        votes.text = itemView.context.resources.getQuantityString(R.plurals.votes, voteCount, voteCount)
        acceptedAnswerCheck.visibility = if (answer.isAccepted) View.VISIBLE else View.GONE
        answerBody.setMarkdown(answer.bodyMarkdown)

        this.username.text = answer.owner.displayName.toHtml()
        GlideApp.with(itemView)
                .load(answer.owner.profileImage)
                .placeholder(R.drawable.user_image_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(userImage)
        this.reputation.text = answer.owner.reputation.toLong().format()
        this.badgeView.badgeCounts = answer.owner.badgeCounts
    }
}
