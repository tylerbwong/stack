package me.tylerbwong.stack.ui.inbox

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.compose.AsyncImage
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.InboxItem
import me.tylerbwong.stack.ui.Header
import me.tylerbwong.stack.ui.utils.compose.StackTheme
import me.tylerbwong.stack.ui.utils.formatFullDate
import me.tylerbwong.stack.ui.utils.toHtml

// TODO Use this when AppBar liftOnScroll interop works
@Composable
fun InboxScreen(
    viewModel: InboxViewModel = viewModel(),
    refreshLayout: SwipeRefreshLayout,
) {
    val context = LocalContext.current
    val items by viewModel.inboxItems.observeAsState(initial = emptyList())
    val unreadCount by viewModel.unreadCount.observeAsState(initial = 0)
    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        refreshLayout.setOnChildScrollUpCallback { _, _ ->
            listState.firstVisibleItemScrollOffset > 0
        }
    }
    StackTheme {
        LazyColumn(
            modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
            state = listState,
            verticalArrangement = spacedBy(16.dp),
        ) {
            item {
                Header(
                    title = stringResource(R.string.inbox),
                    subtitle = stringResource(R.string.unread_count, unreadCount)
                )
            }
            items(items) {
                InboxItem(it) { viewModel.onItemClicked(context, it) }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InboxItem(item: InboxItem, onClick: () -> Unit) {
    BaseInboxItem(
        isUnread = item.isUnread,
        onClick = onClick,
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = item.title.toHtml().toString(),
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
            },
            overlineContent = {
                Column {
                    Text(
                        text = item.itemType
                            .replace("_", " ")
                            .toLowerCase(Locale.current),
                    )
                    Text(text = item.creationDate.formatFullDate())
                }
            },
            supportingContent = {
                item.body?.let {
                    Text(
                        text = it.toHtml().toString(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3,
                    )
                }
            },
            leadingContent = {
                AsyncImage(model = item.site?.iconUrl, contentDescription = item.site?.name)
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaseInboxItem(
    isUnread: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    if (isUnread) {
        Card(
            onClick = onClick,
            content = { content() },
        )
    } else {
        OutlinedCard(
            onClick = onClick,
            content = { content() },
        )
    }
}
