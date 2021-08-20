package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.QuestionTitleHolderBinding
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory
import me.tylerbwong.stack.ui.utils.toHtml

class QuestionTitleHolder(
    parent: ViewGroup
) : DynamicViewBindingHolder<QuestionTitleItem, QuestionTitleHolderBinding>(
    parent,
    QuestionTitleHolderBinding::inflate
) {

    init {
        binding.questionTitle.setSpannableFactory(noCopySpannableFactory)
    }

    override fun QuestionTitleHolderBinding.bind(item: QuestionTitleItem) {
        questionTitle.setLatex(item.title.toHtml().toString())
    }
}
