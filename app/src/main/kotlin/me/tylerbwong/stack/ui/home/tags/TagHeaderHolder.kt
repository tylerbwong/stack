package me.tylerbwong.stack.ui.home.tags

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.TagHeaderItemBinding
import me.tylerbwong.stack.ui.home.TagHeaderItem
import me.tylerbwong.stack.ui.questions.QuestionPage
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class TagHeaderHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<TagHeaderItem, TagHeaderItemBinding>(
    container, TagHeaderItemBinding::inflate
) {
    override fun TagHeaderItemBinding.bind(item: TagHeaderItem) {
        root.setThrottledOnClickListener {
            QuestionsActivity.startActivityForKey(it.context, QuestionPage.TAGS, item.tagName)
        }
        tagHeader.apply {
            elevation = 0f
            stateListAnimator = null
            text = item.tagName
            setThrottledOnClickListener {
                QuestionsActivity.startActivityForKey(it.context, QuestionPage.TAGS, item.tagName)
            }
        }
    }
}
