package me.tylerbwong.stack.ui.search.tags

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import me.tylerbwong.stack.databinding.TagsHolderBinding
import me.tylerbwong.stack.ui.home.TagsItem
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class TagsHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    private val binding = TagsHolderBinding.bind(itemView)
    private val tagsViews = listOf(
        binding.tagsViewTop,
        binding.tagsViewMiddle,
        binding.tagsViewBottom
    )

    fun bind(tagsItem: TagsItem) {
        tagsViews.forEach { it.removeAllViews() }
        val chunkedTags = tagsItem.tags.chunked(tagsItem.tags.size / 3)
        chunkedTags.forEachIndexed { index, tags ->
            tags.forEach {
                tagsViews.getOrElse(index) { binding.tagsViewTop }.addView(
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
