package me.tylerbwong.stack.presentation.questions

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question

class QuestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = ViewCompat.requireViewById(itemView, R.id.title)

    fun bind(question: Question) {
        title.text = question.title
    }
}