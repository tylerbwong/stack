package me.tylerbwong.stack.ui.search.tags

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.TagsHolderBinding
import me.tylerbwong.stack.ui.home.TagsItem
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.createChip

class TagsHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<TagsItem, TagsHolderBinding>(container, TagsHolderBinding::inflate) {

    private val tagsViews = listOf(
        binding.tagsViewTop,
        binding.tagsViewMiddle,
        binding.tagsViewBottom
    )

    override fun TagsHolderBinding.bind(item: TagsItem) {
        tagsViews.forEach { it.removeAllViews() }
        item.tags.forEachIndexed { index, tags ->
            tags.forEach { tag ->
                tagsViews.getOrElse(index) { tagsViewTop }.addView(
                    itemView.context.createChip(tag.name) {
                        QuestionsActivity.startActivityForKey(it.context, TAGS, tag.name)
                    }
                )
            }
        }
    }
}
