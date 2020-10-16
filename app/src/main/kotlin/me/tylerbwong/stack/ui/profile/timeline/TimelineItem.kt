package me.tylerbwong.stack.ui.profile.timeline

import androidx.recyclerview.widget.DiffUtil
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.adapter.ViewHolderProvider
import me.tylerbwong.stack.api.model.TimelineEvent

sealed class TimelineItem(
    val event: TimelineEvent,
    viewHolderProvider: ViewHolderProvider
) : DynamicItem(viewHolderProvider)

class CommentedItem(event: TimelineEvent) : TimelineItem(event, ::CommentedHolder)
class AskedItem(event: TimelineEvent) : TimelineItem(event, ::AskedHolder)
class AnsweredItem(event: TimelineEvent) : TimelineItem(event, ::AnsweredHolder)
class BadgeItem(event: TimelineEvent) : TimelineItem(event, ::BadgeHolder)
class RevisionItem(event: TimelineEvent) : TimelineItem(event, ::RevisionHolder)
class AcceptedItem(event: TimelineEvent) : TimelineItem(event, ::AcceptedHolder)
class ReviewedItem(event: TimelineEvent) : TimelineItem(event, ::ReviewedHolder)
class SuggestedItem(event: TimelineEvent) : TimelineItem(event, ::SuggestedHolder)

object TimelineItemDiffCallback : DiffUtil.ItemCallback<DynamicItem>() {
    override fun areItemsTheSame(oldItem: DynamicItem, newItem: DynamicItem) =
        oldItem is TimelineItem && newItem is TimelineItem &&
                oldItem.event == newItem.event

    override fun areContentsTheSame(oldItem: DynamicItem, newItem: DynamicItem) =
        oldItem is TimelineItem && newItem is TimelineItem &&
                oldItem.event == newItem.event
}
