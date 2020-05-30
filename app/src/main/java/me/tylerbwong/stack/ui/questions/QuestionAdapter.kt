package me.tylerbwong.stack.ui.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.databinding.QuestionHolderBinding
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.inflate
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.systemService
import me.tylerbwong.stack.ui.utils.toHtml

class QuestionAdapter : ListAdapter<Question, QuestionViewHolder>(
    AsyncDifferConfig.Builder(
        object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(
                oldItem: Question,
                newItem: Question
            ) = oldItem.questionId == newItem.questionId

            override fun areContentsTheSame(
                oldItem: Question,
                newItem: Question
            ) = oldItem.title == newItem.title && oldItem.answerCount == newItem.answerCount &&
                    oldItem.owner == newItem.owner
        }
    ).build()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = QuestionViewHolder(parent.inflate(R.layout.question_holder))

    override fun onBindViewHolder(
        holder: QuestionViewHolder,
        position: Int
    ) = holder.bind(getItem(position))
}

class QuestionViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    private val binding = QuestionHolderBinding.bind(itemView)

    fun bind(question: Question) {
        binding.questionTitle.text = question.title.toHtml()
        binding.answerCount.text = question.answerCount.toString()

        binding.ownerView.bind(question.owner)

        itemView.setOnLongClickListener { view ->
            val context = view.context
            context.systemService<ClipboardManager>(Context.CLIPBOARD_SERVICE)?.let {
                it.setPrimaryClip(ClipData.newPlainText(LABEL, question.shareLink))
                Toast.makeText(context, R.string.link_copied, Toast.LENGTH_SHORT).show()
            }
            true
        }

        itemView.setThrottledOnClickListener {
            QuestionDetailActivity.startActivity(it.context, question.questionId)
        }
    }

    companion object {
        private const val LABEL = "linkText"
    }
}
