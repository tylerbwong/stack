package me.tylerbwong.stack.presentation.answers

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.presentation.utils.inflateWithoutAttaching

class AnswerAdapter : RecyclerView.Adapter<AnswerHolder>() {
    internal var answers: List<Answer> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerHolder {
        val view = parent.inflateWithoutAttaching(R.layout.answer_holder)
                ?: throw IllegalStateException("Could not inflate question view")
        return AnswerHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerHolder, position: Int) {
        holder.bind(answers[position])
    }

    override fun getItemCount() = answers.size
}
