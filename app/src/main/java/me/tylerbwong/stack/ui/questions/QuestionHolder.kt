package me.tylerbwong.stack.ui.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.question_holder.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.inflate
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class QuestionHolder(parent: ViewGroup) : DynamicViewHolder(
    parent.inflate(R.layout.question_holder)
) {
    override fun bind(data: Any) {
        (data as? QuestionDataModel)?.let { dataModel ->
            questionTitle.text = dataModel.questionTitle.toHtml()
            answerCount.text = dataModel.answerCount.toString()

            questionBody.apply {
                text = dataModel.questionBody?.toHtml()
                setTextIsSelectable(false)
            }

            ownerView.bind(dataModel.owner)

            itemView.setOnLongClickListener {
                val context = it.context
                val contentManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                contentManager.setPrimaryClip(
                    ClipData.newPlainText("linkText", dataModel.shareLink)
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
