package me.tylerbwong.stack.ui.questions.detail

import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.FragmentActivity
import com.soywiz.klock.seconds
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.FooterHolderBinding
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.formatElapsedTime
import me.tylerbwong.stack.ui.utils.ofType
import me.tylerbwong.stack.ui.utils.toHtml

class FooterHolder(
    parent: ViewGroup
) : DynamicViewBindingHolder<FooterItem, FooterHolderBinding>(
    parent,
    FooterHolderBinding::inflate
) {
    override fun FooterHolderBinding.bind(item: FooterItem) {
        creationDate.apply {
            text = context.getString(
                R.string.asked,
                item.creationDate.seconds.millisecondsLong.formatElapsedTime(context)
            )
        }
        lastEditor.apply {
            isInvisible = item.lastEditor == null
            text = context.getString(
                R.string.last_edited_by,
                item.lastEditor?.displayName?.toHtml()?.toString()
            )
        }
        commentCount.apply {
            text = (item.commentCount ?: 0).toLong().format()
            setOnClickListener {
                it.context.ofType<FragmentActivity>()?.let { activity ->
                    CommentsBottomSheetDialogFragment.show(
                        activity.supportFragmentManager,
                        item.entityId
                    )
                }
            }
        }
        ownerView.bind(item.owner)
    }
}
