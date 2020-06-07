package me.tylerbwong.stack.ui.search.tags

import android.view.ViewGroup
import com.google.android.material.chip.Chip
import me.tylerbwong.stack.databinding.TagsHolderBinding
import me.tylerbwong.stack.ui.adapter.ViewBindingViewHolder
import me.tylerbwong.stack.ui.home.TagsItem
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class TagsHolder(
    container: ViewGroup
) : ViewBindingViewHolder<TagsItem, TagsHolderBinding>(container, TagsHolderBinding::inflate) {

    private val tagsViews = listOf(
        binding.tagsViewTop,
        binding.tagsViewMiddle,
        binding.tagsViewBottom
    )

    override fun TagsHolderBinding.bind(item: TagsItem) {
        tagsViews.forEach { it.removeAllViews() }
        val chunkedTags = item.tags.chunked(item.tags.size / 3)
        chunkedTags.forEachIndexed { index, tags ->
            tags.forEach {
                tagsViews.getOrElse(index) { tagsViewTop }.addView(
                    Chip(itemView.context).apply {
                        text = it.name
                        setThrottledOnClickListener { view ->
                            QuestionsActivity.startActivityForKey(view.context, TAGS, it.name)
                        }
                    }
                )
            }
        }
    }
}
