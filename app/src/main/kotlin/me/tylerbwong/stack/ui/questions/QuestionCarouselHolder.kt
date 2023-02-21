package me.tylerbwong.stack.ui.questions

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionCarouselItemBinding
import me.tylerbwong.stack.ui.home.HomeItemDiffCallback
import me.tylerbwong.stack.ui.home.QuestionCarouselItem
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration

class QuestionCarouselHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<QuestionCarouselItem, QuestionCarouselItemBinding>(
    container,
    QuestionCarouselItemBinding::inflate,
) {
    private val adapter = DynamicListAdapter(HomeItemDiffCallback)
    override fun QuestionCarouselItemBinding.bind(item: QuestionCarouselItem) {
        root.adapter = adapter
        root.layoutManager = LinearLayoutManager(root.context, HORIZONTAL, false)
        if (root.itemDecorationCount == 0) {
            root.addItemDecoration(
                ViewHolderItemDecoration(
                    spacing = root.context.resources.getDimensionPixelSize(
                        R.dimen.item_spacing_question_detail
                    ),
                )
            )
        }
        adapter.submitList(item.questions)
    }
}
