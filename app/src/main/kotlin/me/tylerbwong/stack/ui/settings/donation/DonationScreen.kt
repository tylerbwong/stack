package me.tylerbwong.stack.ui.settings.donation

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.compose.StackTheme
import me.tylerbwong.stack.ui.utils.ofType

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationScreen(viewModel: DonationViewModel = viewModel(), onBackPressed: () -> Unit) {
    val items by viewModel.availableProducts.observeAsState(emptyList())
    val purchaseSuccess by viewModel.purchaseSuccess.observeAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current
    val appIcon = remember {
        try {
            context
                .packageManager
                .getApplicationIcon(BuildConfig.APPLICATION_ID)
                .toBitmap()
                .asImageBitmap()
        } catch (_: Exception) {
            null
        }
    }
    StackTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.support_development)) },
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
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = spacedBy(16.dp),
            ) {
                if (appIcon != null) {
                    item {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Image(
                                bitmap = appIcon,
                                contentDescription = stringResource(R.string.app_name),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .shadow(elevation = 2.dp, shape = CircleShape),
                            )
                        }
                    }
                }
                item {
                    Text(
                        text = stringResource(R.string.support_development_message),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                items(items) { product ->
                    OutlinedCard(
                        onClick = {
                            context.ofType<Activity>()?.let {
                                viewModel.launchBillingFlow(it, product)
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                    ) {
                        // Hack for lost padding due to multi-line supporting content
                        var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }
                        ListItem(
                            headlineContent = { Text(text = product.name) },
                            modifier = if ((textLayout?.lineCount ?: 0) > 1) {
                                Modifier.padding(vertical = 8.dp)
                            } else {
                                Modifier
                            },
                            supportingContent = {
                                Text(
                                    text = product.description,
                                    onTextLayout = { textLayout = it },
                                )
                            },
                            trailingContent = {
                                Text(
                                    text = product.formattedPrice
                                        ?: stringResource(R.string.support_development_price_unavailable),
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        )
                    }
                }
            }
        }

        if (purchaseSuccess != null) {
            AlertDialog(
                onDismissRequest = { viewModel.markConfirmationSeen() },
                confirmButton = {},
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.markConfirmationSeen() },
                    ) {
                        Text(text = stringResource(R.string.dismiss))
                    }
                },
                icon = if (purchaseSuccess == true) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                        )
                    }
                } else {
                    null
                },
                title = {
                    Text(
                        text = stringResource(
                            if (purchaseSuccess == true) {
                                R.string.support_development_success_title
                            } else {
                                R.string.support_development_failure_title
                            }
                        )
                    )
                },
                text = {
                    Text(
                        text = stringResource(
                            if (purchaseSuccess == true) {
                                R.string.support_development_success_message
                            } else {
                                R.string.support_development_failure_message
                            }
                        )
                    )
                },
            )
        }
    }
}
