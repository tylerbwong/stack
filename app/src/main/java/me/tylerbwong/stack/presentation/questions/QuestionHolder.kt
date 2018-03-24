package me.tylerbwong.stack.presentation.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.presentation.owners.BadgeView
import me.tylerbwong.stack.presentation.utils.toHtml

class QuestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val questionTitle: TextView = ViewCompat.requireViewById(itemView, R.id.questionTitle)
    private val questionBody: TextView = ViewCompat.requireViewById(itemView, R.id.questionBody)
    private val username: TextView = ViewCompat.requireViewById(itemView, R.id.username)
    private val badgeView: BadgeView = ViewCompat.requireViewById(itemView, R.id.badgeView)

    fun bind(question: Question) {
        this.questionTitle.text = question.title.toHtml()
        this.username.text = itemView.context.getString(R.string.by, question.owner.displayName).toHtml()
        this.badgeView.badgeCounts = question.owner.badgeCounts
        this.questionBody.text = question.body?.toHtml()

        itemView.setOnLongClickListener {
            val contentManager = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            contentManager.primaryClip = ClipData.newPlainText("linkText", question.shareLink)
            Toast.makeText(it.context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
            true
        }

        itemView.setOnClickListener {
            // TODO link to question detail activity
        }
    }
}