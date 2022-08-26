package me.tylerbwong.stack.ui.settings.libraries

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import me.tylerbwong.stack.R
import me.tylerbwong.stack.markdown.MarkdownTextView
import me.tylerbwong.stack.ui.utils.compose.StackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrariesScreen(libraries: LiveData<List<LibraryItem>>, onBackPressed: () -> Unit) {
    StackTheme {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text(text = stringResource(R.string.libraries)) },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                )
            },
        ) {
            var clickedLibraryItem by remember { mutableStateOf<LibraryItem?>(null) }
            val items by libraries.observeAsState(initial = emptyList())
            LazyColumn(modifier = Modifier.padding(it)) {
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
                indication = if (isSystemInDarkTheme()) {
                    rememberRipple(color = Color.White)
                } else {
                    rememberRipple()
                },
                onClick = showLicenseDialog
            ),
        secondaryText = { Text(text = library.author) },
        text = { Text(text = library.name) },
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
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Vertical
                    )
            ) {
                AndroidView(
                    factory = {
                        MarkdownTextView(it).apply {
                            setMarkdown(licenseText)
                        }
                    },
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(android.R.string.ok))
            }
        },
        shape = RoundedCornerShape(8.dp),
    )
}
