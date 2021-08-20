package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.QuestionTagsHolderBinding
import me.tylerbwong.stack.ui.questions.QuestionPage
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.createChip

class QuestionTagsHolder(
    parent: ViewGroup
) : DynamicViewBindingHolder<QuestionTagsItem, QuestionTagsHolderBinding>(
    parent,
    QuestionTagsHolderBinding::inflate
) {
    override fun QuestionTagsHolderBinding.bind(item: QuestionTagsItem) {
        tagsView.removeAllViews()
        item.tags.forEach { tag ->
            tagsView.addView(
                itemView.context.createChip(tag) {
                    QuestionsActivity.startActivityForKey(it.context, QuestionPage.TAGS, tag)
                }
            )
        }
    }
}
