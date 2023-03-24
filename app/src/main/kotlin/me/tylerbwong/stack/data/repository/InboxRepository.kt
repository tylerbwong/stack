package me.tylerbwong.stack.data.repository

import androidx.annotation.StringDef
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import me.tylerbwong.stack.api.model.InboxItem
import me.tylerbwong.stack.api.service.InboxService
import javax.inject.Inject
import javax.inject.Singleton

const val ALL = "all"
const val UNREAD = "unread"

@StringDef(ALL, UNREAD)
annotation class InboxFilter

@Singleton
class InboxRepository @Inject constructor(
    private val inboxService: InboxService,
) {
    internal val inboxItems: LiveData<List<InboxItem>>
        get() = _inboxItems
    private val _inboxItems = MutableLiveData<List<InboxItem>>()

    internal val unreadCount: LiveData<Int> = inboxItems.map { item -> item.count { it.isUnread } }

    suspend fun fetchInbox(@InboxFilter filter: String = ALL) {
        _inboxItems.value = inboxService.getInbox().items
            .filter { if (filter == UNREAD) it.isUnread else true }
            .sortedByDescending { it.creationDate }
    }
}
