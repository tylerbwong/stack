package me.tylerbwong.stack.ui.search.tags

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.tags_holder.*
import me.tylerbwong.stack.ui.home.TagsItem
import me.tylerbwong.stack.ui.questions.QuestionPage.*
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class TagsHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val tagsViews = listOf(tagsViewTop, tagsViewMiddle, tagsViewBottom)

    fun bind(tagsItem: TagsItem)  {
        val chunkedTags = tagsItem.tags.chunked(tagsItem.tags.size / 3)
        chunkedTags.forEachIndexed { index, tags ->
            tags.forEach {
                tagsViews.getOrElse(index) { tagsViewTop }.addView(
                    Chip(containerView.context).apply {
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
