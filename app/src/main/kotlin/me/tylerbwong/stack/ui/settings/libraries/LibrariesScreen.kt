package me.tylerbwong.stack.ui.settings.libraries

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import me.tylerbwong.stack.R
import me.tylerbwong.stack.markdown.MarkdownTextView
import me.tylerbwong.stack.ui.theme.ThemeManager.isNightModeEnabled

@Composable
fun LibrariesScreen(libraries: LiveData<List<LibraryItem>>, onBackPressed: () -> Unit) {
    val viewBackgroundColor = colorResource(R.color.viewBackgroundColor)
    val primaryTextColor = colorResource(R.color.primaryTextColor)
    val iconColor = colorResource(R.color.iconColor)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.libraries),
                        color = primaryTextColor,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = iconColor
                        )
                    }
                },
                backgroundColor = viewBackgroundColor,
            )
        },
        backgroundColor = viewBackgroundColor,
    ) {
        var clickedLibraryItem by remember { mutableStateOf<LibraryItem?>(null) }
        val items by libraries.observeAsState(initial = emptyList())
        LazyColumn {
            items(
                items = items,
                key = null,
                itemContent = { library ->
                    LibraryItem(library = library) {
                        clickedLibraryItem = library
                    }
                }
            )
        }

        val library = clickedLibraryItem
        if (library != null) {
            val hideDialog = { clickedLibraryItem = null }
            LicenseDialog(
                libraryName = library.name,
                licenseText = library.licenseText,
                onDismissRequest = hideDialog,
                onConfirm = hideDialog,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LibraryItem(
    library: LibraryItem,
    showLicenseDialog: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    ListItem(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = if (LocalContext.current.isNightModeEnabled) {
                    rememberRipple(color = Color.White)
                } else {
                    rememberRipple()
                },
                onClick = showLicenseDialog
            ),
        secondaryText = {
            Text(
                text = library.author,
                color = colorResource(R.color.secondaryTextColor),
            )
        },
        text = {
            Text(
                text = library.name,
                color = colorResource(R.color.primaryTextColor),
            )
        },
    )
}

@Composable
private fun LicenseDialog(
    libraryName: String,
    licenseText: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(R.string.license, libraryName),
                color = colorResource(R.color.primaryTextColor),
                style = MaterialTheme.typography.h6,
            )
        },
        text = {
            LazyColumn {
                item {
                    AndroidView(
                        factory = {
                            MarkdownTextView(it).apply {
                                setMarkdown(licenseText)
                            }
                        },
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(android.R.string.ok),
                    color = colorResource(R.color.primaryTextColor),
                )
            }
        },
        shape = RoundedCornerShape(8.dp),
        backgroundColor = colorResource(R.color.dialogBackgroundColor),
    )
}
