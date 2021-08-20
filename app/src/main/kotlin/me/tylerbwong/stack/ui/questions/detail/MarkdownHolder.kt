package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.MarkdownHolderBinding
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory

class MarkdownHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<MarkdownItem, MarkdownHolderBinding>(
    container,
    MarkdownHolderBinding::inflate
) {
    init {
        binding.markdownTextView.setSpannableFactory(noCopySpannableFactory)
    }

    override fun MarkdownHolderBinding.bind(item: MarkdownItem) {
        markdownTextView.setMarkdown(item.render(markdownTextView))
    }
}
