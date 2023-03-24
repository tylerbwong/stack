package me.tylerbwong.stack.ui.inbox

import android.view.ViewGroup
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.InboxItemHolderBinding
import me.tylerbwong.stack.ui.home.InboxHomeItem
import me.tylerbwong.stack.ui.utils.compose.StackTheme

class InboxItemHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<InboxHomeItem, InboxItemHolderBinding>(
    container,
    InboxItemHolderBinding::inflate
) {
    override fun InboxItemHolderBinding.bind(item: InboxHomeItem) {
        composeContent.setContent {
            StackTheme {
                InboxItem(item = item.inboxItem) {
                    item.onItemClicked(itemView.context, item.inboxItem)
                }
            }
        }
    }
}
