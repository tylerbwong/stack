package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.FencedCodeBlockHolderBinding
import me.tylerbwong.stack.ui.utils.noCopySpannableFactory

class FencedCodeBlockHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<FencedCodeBlockItem, FencedCodeBlockHolderBinding>(
    container,
    FencedCodeBlockHolderBinding::inflate
) {
    init {
        binding.codeBlock.setSpannableFactory(noCopySpannableFactory)
    }

    override fun FencedCodeBlockHolderBinding.bind(item: FencedCodeBlockItem) {
        codeBlock.setMarkdown(item.render(codeBlock))
    }
}
