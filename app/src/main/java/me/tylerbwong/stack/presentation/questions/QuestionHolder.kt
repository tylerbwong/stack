package me.tylerbwong.stack.presentation.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.toHtml


class QuestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val question: TextView = ViewCompat.requireViewById(itemView, R.id.question)
    private val username: TextView = ViewCompat.requireViewById(itemView, R.id.username)
    private val expandCollapseArrow: ImageView = ViewCompat.requireViewById(itemView, R.id.expandCollapseButton)
    private val tagLayout: LinearLayout = ViewCompat.requireViewById(itemView, R.id.tagLayout)
    private val tagsChipGroup: ChipGroup = ViewCompat.requireViewById(itemView, R.id.tags)

    fun bind(question: Question) {
        this.question.text = question.title.toHtml()
        this.username.text = itemView.context.getString(R.string.by, question.owner.displayName)

        setExpanded(itemView.context, question.isExpanded, false)

        itemView.setOnLongClickListener {
            val contentManager = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            contentManager.primaryClip = ClipData.newPlainText("linkText", question.shareLink)
            Toast.makeText(it.context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
            true
        }

        expandCollapseArrow.setOnClickListener {
            question.isExpanded = !question.isExpanded
            setExpanded(it.context, question.isExpanded)
        }

        question.tags.forEach {
            with (Chip(itemView.context)) {
                chipText = it
                with(itemView.context.resources.getDimension(R.dimen.item_spacing)) {
                    chipStartPadding = this
                    chipEndPadding = this
                }
                with (itemView.context.resources.getDimension(R.dimen.chip_text_spacing)) {
                    textStartPadding = this
                    textEndPadding = this
                }
                tagsChipGroup.addView(this)
            }
        }
    }

    private fun setExpanded(context: Context, isExpanded: Boolean, animate: Boolean = true) {
        if (!animate) {
            if (!isExpanded) {
                expandCollapseArrow.setImageResource(R.drawable.down_arrow)
            } else {
                expandCollapseArrow.setImageResource(R.drawable.up_arrow)
            }
        } else {
            val vectorDrawable: AnimatedVectorDrawable?

            if (!isExpanded) {
                vectorDrawable = context.getDrawable(R.drawable.up_to_down_arrow) as AnimatedVectorDrawable
                expandCollapseArrow.setImageDrawable(vectorDrawable)
            }
            else {
                vectorDrawable = context.getDrawable(R.drawable.down_to_up_arrow) as AnimatedVectorDrawable
                expandCollapseArrow.setImageDrawable(vectorDrawable)
            }

            vectorDrawable.start()

            TransitionManager.beginDelayedTransition(tagsChipGroup, AutoTransition())
        }

        tagLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
    }
}