@file:Suppress("MagicNumber")

package me.tylerbwong.stack.ui.settings.sites

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.ui.utils.compose.StackTheme

// TODO Switch to Compose TopAppBar once proper SearchView is supported
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun SitesScreen(changeSite: (String) -> Unit, onBackPressed: () -> Unit) {
    val viewModel = viewModel<SitesViewModel>()
    var searchQuery by rememberSaveable { mutableStateOf(viewModel.currentQuery ?: "") }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }

    StackTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (isSearchActive) {
                            TextField(
                                value = searchQuery,
                                onValueChange = {
                                    viewModel.fetchSites(it)
                                    searchQuery = it
                                },
                                modifier = Modifier
                                    .onFocusChanged {
                                        isSearchFocused = it.isFocused
                                    },
                                label = { Text(text = stringResource(R.string.search)) },
                                textStyle = MaterialTheme.typography.bodyLarge,
                            )
                        } else {
                            Text(text = stringResource(R.string.sites))
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (isSearchActive) {
                                    isSearchActive = false
                                } else {
                                    onBackPressed()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        if (!isSearchActive) {
                            IconButton(
                                onClick = { isSearchActive = true },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                )
                            }
                        } else if (searchQuery.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    viewModel.fetchSites()
                                    searchQuery = ""
                                },
                            ) {
                                if (searchQuery.isNotEmpty()) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = null,
                                    )
                                }
                            }
                        }
                    },
                )
            },
        ) { SitesLayout(modifier = Modifier.padding(it), changeSite) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SitesLayout(
    modifier: Modifier = Modifier,
    changeSite: (String) -> Unit,
    isCompact: Boolean = false,
) {
    val viewModel = viewModel<SitesViewModel>()
    val associatedSites by viewModel.associatedSites.observeAsState(initial = emptyList())
    val sites by viewModel.sites.observeAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.observeAsState()
    val listState = rememberLazyListState()

    StackTheme {
        LazyColumn(
            modifier = modifier.nestedScroll(rememberNestedScrollInteropConnection()),
            state = listState,
        ) {
            if (associatedSites.isNotEmpty()) {
                stickyHeader {
                    SitesHeader(
                        headerResId = R.string.my_sites,
                    )
                }
                items(items = associatedSites) { site ->
                    SiteItem(
                        site = site,
                        searchQuery = searchQuery,
                        isCompact = isCompact,
                    ) { changeSite(site.parameter) }
                }
            }
            if (sites.isNotEmpty()) {
                if (associatedSites.isNotEmpty()) {
                    stickyHeader {
                        SitesHeader(
                            headerResId = R.string.other_sites,
                        )
                    }
                }
                items(items = sites) { site ->
                    SiteItem(
                        site = site,
                        searchQuery = searchQuery,
                        isCompact = isCompact,
                    ) { changeSite(site.parameter) }
                }
            }
        }
    }
}

@Composable
fun SitesHeader(@StringRes headerResId: Int) {
    Text(
        text = stringResource(headerResId),
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium,
    )
}

// TODO Migrate to ListItem
@Composable
fun SiteItem(
    site: Site,
    searchQuery: String?,
    isCompact: Boolean,
    onItemClicked: () -> Unit,
) {
    if (isCompact) {
        CompactSiteItem(site = site, onItemClicked = onItemClicked)
    } else {
        val interactionSource = remember { MutableInteractionSource() }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = if (isSystemInDarkTheme()) {
                        rememberRipple(color = Color.White)
                    } else {
                        rememberRipple()
                    },
                    onClick = onItemClicked
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = site.iconUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(
                        start = 8.dp,
                        top = 8.dp,
                        end = 4.dp,
                        bottom = 8.dp
                    ),
            )
            Column(
                modifier = Modifier
                    .padding(
                        start = 2.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = site.name.toAnnotatedString(searchQuery),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = site.audience.toAnnotatedString(searchQuery),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun CompactSiteItem(
    site: Site,
    onItemClicked: () -> Unit,
) {
    val viewModel = viewModel<SitesViewModel>()
    val currentSite by viewModel.currentSite.observeAsState()
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 24.dp)
            .background(
                color = if (site.parameter == currentSite?.parameter) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
            )
            .clip(shape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = if (isSystemInDarkTheme()) {
                    rememberRipple(color = Color.White)
                } else {
                    rememberRipple()
                },
                onClick = onItemClicked
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = site.iconUrl,
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .padding(
                    start = 8.dp,
                    top = 8.dp,
                    end = 4.dp,
                    bottom = 8.dp
                ),
        )
        Column(
            modifier = Modifier
                .padding(
                    start = 2.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = site.name,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

private fun String.toAnnotatedString(query: String?): AnnotatedString {
    return if (!query.isNullOrEmpty()) {
        val startIndex = indexOf(query, startIndex = 0, ignoreCase = true)
        if (startIndex != -1) {
            val endIndex = startIndex + query.length
            with(AnnotatedString.Builder(this)) {
                addStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold),
                    start = startIndex,
                    end = endIndex
                )
                toAnnotatedString()
            }
        } else {
            AnnotatedString(this)
        }
    } else {
        AnnotatedString(this)
    }
}
