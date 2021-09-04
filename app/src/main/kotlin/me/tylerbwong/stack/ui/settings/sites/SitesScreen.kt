@file:Suppress("MagicNumber")
package me.tylerbwong.stack.ui.settings.sites

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.ui.theme.ThemeManager.isNightModeEnabled

// TODO Switch to Compose TopAppBar once proper SearchView is supported
@Suppress("LongMethod")
@Composable
fun SitesScreen(changeSite: (String) -> Unit, onBackPressed: () -> Unit) {
    val viewBackgroundColor = colorResource(R.color.viewBackgroundColor)
    val primaryTextColor = colorResource(R.color.primaryTextColor)
    val secondaryTextColor = colorResource(R.color.secondaryTextColor)
    val iconColor = colorResource(R.color.iconColor)
    val colorAccent = colorResource(R.color.colorAccent)

    val viewModel = viewModel<SitesViewModel>()
    var searchQuery by rememberSaveable { mutableStateOf(viewModel.currentQuery ?: "") }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }

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
                            label = {
                                Text(
                                    text = stringResource(R.string.search),
                                    color = if (isSearchFocused) {
                                        viewBackgroundColor
                                    } else {
                                        secondaryTextColor
                                    },
                                )
                            },
                            textStyle = MaterialTheme.typography.body1.copy(color = primaryTextColor),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = viewBackgroundColor,
                                focusedIndicatorColor = colorAccent,
                                focusedLabelColor = colorAccent,
                            )
                        )
                    } else {
                        Text(text = stringResource(R.string.sites), color = primaryTextColor)
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
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = iconColor
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
                                tint = iconColor
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
                                    tint = iconColor
                                )
                            }
                        }
                    }
                },
                backgroundColor = viewBackgroundColor,
            )
        },
        backgroundColor = viewBackgroundColor,
    ) { SitesLayout(changeSite) }
}

@Composable
fun SitesLayout(changeSite: (String) -> Unit) {
    var clickedSite by remember { mutableStateOf<Site?>(null) }
    val viewModel = viewModel<SitesViewModel>()
    val sites by viewModel.sites.observeAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.observeAsState()

    LazyColumn {
        items(items = sites) { site ->
            SiteItem(
                site = site,
                searchQuery = searchQuery,
            ) {
                if (viewModel.isAuthenticated) {
                    clickedSite = site
                } else {
                    changeSite(site.parameter)
                }
            }
        }
    }

    val site = clickedSite
    if (site != null) {
        ChangeSiteDialog(
            onDismissRequest = { clickedSite = null },
            onConfirm = { viewModel.logOut(site.parameter) },
            onDismiss = { clickedSite = null },
        )
    }
}

// TODO Migrate to ListItem
@OptIn(ExperimentalCoilApi::class)
@Composable
fun SiteItem(
    site: Site,
    searchQuery: String?,
    onItemClicked: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = if (LocalContext.current.isNightModeEnabled) {
                    rememberRipple(color = Color.White)
                } else {
                    rememberRipple()
                },
                onClick = onItemClicked
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberImagePainter(data = site.iconUrl),
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
                color = colorResource(R.color.primaryTextColor),
                style = MaterialTheme.typography.body1,
            )
            Text(
                text = site.audience.toAnnotatedString(searchQuery),
                color = colorResource(R.color.secondaryTextColor),
                style = MaterialTheme.typography.body2,
            )
        }
    }
}

@Composable
private fun ChangeSiteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(R.string.log_out_title),
                color = colorResource(R.color.primaryTextColor),
                style = MaterialTheme.typography.h6,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.log_out_site_switch),
                color = colorResource(R.color.primaryTextColor),
                style = MaterialTheme.typography.body1,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.log_out),
                    color = colorResource(R.color.primaryTextColor),
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = colorResource(R.color.primaryTextColor),
                )
            }
        },
        shape = RoundedCornerShape(8.dp),
        backgroundColor = colorResource(R.color.dialogBackgroundColor),
    )
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
