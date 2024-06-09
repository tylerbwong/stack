package me.tylerbwong.stack.ui.settings.libraries

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.util.author
import com.mikepenz.aboutlibraries.util.withContext
import kotlinx.collections.immutable.toImmutableList
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.compose.StackTheme
import me.tylerbwong.stack.ui.utils.launchUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrariesScreen(onBackPressed: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current
    StackTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.libraries)) },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { paddingValues ->
            LibrariesContainer(
                contentPadding = paddingValues,
                librariesBlock = { context ->
                    val libs = Libs.Builder()
                        .withContext(context)
                        .build()
                    libs.copy(
                        libraries = libs
                            .libraries
                            .distinctBy { it.name + it.author + it.artifactVersion }
                            .toImmutableList()
                    )
                },
                colors = LibraryDefaults.libraryColors(
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    badgeBackgroundColor = MaterialTheme.colorScheme.primary,
                    badgeContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                padding = LibraryDefaults.libraryPadding(
                    badgeContentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                ),
                onLibraryClick = {
                    val website = it.website
                    if (!website.isNullOrEmpty()) {
                        context.launchUrl(url = website)
                    }
                },
            )
        }
    }
}
