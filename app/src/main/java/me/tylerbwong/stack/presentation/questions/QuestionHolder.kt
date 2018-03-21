package me.tylerbwong.stack.presentation.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.toHtml


class QuestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = ViewCompat.requireViewById(itemView, R.id.title)
    private val tags: ChipGroup = ViewCompat.requireViewById(itemView, R.id.tags)

    fun bind(question: Question) {
        title.text = question.title.toHtml()

        itemView.setOnLongClickListener {
            val contentManager = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            contentManager.primaryClip = ClipData.newPlainText("linkText", question.shareLink)
            Toast.makeText(it.context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
            true
        }

        question.tags.forEach {
            with (Chip(itemView.context)) {
                text = it
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                with(itemView.context.resources.getDimension(R.dimen.item_spacing)) {
                    chipStartPadding = this
                    chipEndPadding = this
                }
                with (itemView.context.resources.getDimension(R.dimen.chip_text_spacing)) {
                    textStartPadding = this
                    textEndPadding = this
                }
                tags.addView(this)
            }
        }
    }
}