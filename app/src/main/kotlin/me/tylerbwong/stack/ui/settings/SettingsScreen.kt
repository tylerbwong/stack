@file:Suppress("LongMethod")
package me.tylerbwong.stack.ui.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.chuckerteam.chucker.api.Chucker
import me.tylerbwong.compose.preference.ListPreference
import me.tylerbwong.compose.preference.Preference
import me.tylerbwong.compose.preference.PreferenceCategory
import me.tylerbwong.compose.preference.PreferenceScreen
import me.tylerbwong.compose.preference.SwitchPreference
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.settings.libraries.LibrariesActivity
import me.tylerbwong.stack.ui.settings.sites.SitesActivity
import me.tylerbwong.stack.ui.theme.ThemeManager.delegateMode
import me.tylerbwong.stack.ui.theme.nightModeOptions
import me.tylerbwong.stack.ui.utils.launchUrl
import me.tylerbwong.stack.ui.utils.toHtml
import java.util.Locale
import me.tylerbwong.stack.api.BuildConfig as ApiBuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferences: SharedPreferences,
    viewModel: SettingsViewModel = viewModel(),
    onBackPressed: () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) {
           darkColorScheme()
        } else {
            lightColorScheme()
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = stringResource(R.string.settings)) },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                )
            },
        ) {
            val context = LocalContext.current
            val user by viewModel.user.observeAsState()
            val currentSite by viewModel.currentSite.observeAsState()
            var isSnackbarVisible by remember { mutableStateOf(false) }

            PreferenceScreen(modifier = Modifier.padding(it)) {
                PreferenceCategory(
                    header = { Text(text = stringResource(R.string.account)) }
                ) {
                    user?.let {
                        Preference(
                            title = { Text(text = it.displayName) },
                            summary = it.location?.let { { Text(text = it) } },
                            icon = {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(it.profileImage)
                                        .transformations(CircleCropTransformation())
                                        .size(
                                            context.resources.getDimensionPixelSize(
                                                R.dimen.user_image_placeholder_size
                                            )
                                        ),
                                    contentDescription = null
                                )
                            },
                            onClick = {},
                        )
                    } ?: run {
                        Preference(
                            title = { Text(text = stringResource(R.string.log_in)) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                )
                            },
                            onClick = {},
                        )
                    }
                }
                currentSite?.let { site ->
                    PreferenceCategory(
                        header = { Text(text = stringResource(R.string.current_site)) }
                    ) {
                        Preference(
                            title = { Text(text = site.name.toHtml().toString()) },
                            summary = {
                                Text(
                                    text = site.audience
                                        .replaceFirstChar {
                                            if (it.isLowerCase()) {
                                                it.titlecase(Locale.getDefault())
                                            } else {
                                                it.toString()
                                            }
                                        }
                                        .toHtml()
                                        .toString()
                                )
                            },
                            icon = { AsyncImage(model = site.iconUrl, contentDescription = null) },
                            onClick = { SitesActivity.startActivity(context) },
                        )
                    }
                }

                if (BuildConfig.DEBUG) {
                    PreferenceCategory(
                        header = { Text(text = stringResource(R.string.experimental)) }
                    ) {
                        SwitchPreference(
                            checked = false,
                            title = { Text(text = stringResource(R.string.syntax_highlighting)) },
                            onCheckedChange = { isSnackbarVisible = true },
                            summary = {
                                Text(text = stringResource(R.string.syntax_highlighting_summary))
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = null,
                                )
                            },
                        )
                    }
                    PreferenceCategory(
                        header = { Text(text = stringResource(R.string.debug)) }
                    ) {
                        Preference(
                            title = {
                                Text(text = stringResource(R.string.inspect_network_traffic))
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Traffic,
                                    contentDescription = null,
                                )
                            },
                            onClick = {
                                val intent = Chucker.getLaunchIntent(context)
                                context.startActivity(intent)
                            },
                        )
                    }
                }

                PreferenceCategory(
                    header = { Text(text = stringResource(R.string.app)) }
                ) {
                    ListPreference(
                        selectedIndex = nightModeOptions.values.indexOf(context.delegateMode),
                        title = { Text(text = stringResource(R.string.theme)) },
                        dialogTitle = { Text(text = stringResource(R.string.theme)) },
                        items = nightModeOptions.keys.map { context.getString(it) },
                        onConfirm = { _, which ->
                            val newMode = nightModeOptions.values.toList()[which]
                            AppCompatDelegate.setDefaultNightMode(newMode)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Brightness2,
                                contentDescription = null,
                            )
                        },
                    )
                    val versionCode = context.resources.getInteger(R.integer.version_code)
                    val summary = "${BuildConfig.VERSION_NAME} ${
                        context.getString(
                            R.string.item_count,
                            versionCode
                        )
                    }"
                    Preference(
                        title = { Text(text = stringResource(R.string.version)) },
                        summary = { Text(text = summary) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                            )
                        },
                    )
                }
                PreferenceCategory(
                    header = { Text(text = stringResource(R.string.about)) },
                    divider = null,
                ) {
                    Preference(
                        title = { Text(text = stringResource(R.string.source)) },
                        summary = { Text(text = stringResource(R.string.source_description)) },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_github),
                                contentDescription = null,
                            )
                        },
                        onClick = { context.launchUrl(context.getString(R.string.repository_url)) },
                    )
                    Preference(
                        title = { Text(text = stringResource(R.string.libraries)) },
                        summary = { Text(text = stringResource(R.string.libraries_description)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = null,
                            )
                        },
                        onClick = { LibrariesActivity.startActivity(context) },
                    )
                    Preference(
                        title = { Text(text = stringResource(R.string.api)) },
                        summary = { Text(text = "v${ApiBuildConfig.API_VERSION}") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Cloud,
                                contentDescription = null,
                            )
                        },
                        onClick = { context.launchUrl(context.getString(R.string.api_home_url)) },
                    )
                    Preference(
                        title = { Text(text = stringResource(R.string.privacy)) },
                        onClick = { context.launchUrl(context.getString(R.string.privacy_url)) },
                    )
                    Preference(
                        title = { Text(text = stringResource(R.string.terms)) },
                        onClick = { context.launchUrl(context.getString(R.string.terms_url)) },
                    )
                }
            }

            if (isSnackbarVisible) {
                // TODO Show Snackbar
            }
        }
    }
}
