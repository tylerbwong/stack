@file:Suppress("FunctionNaming", "LongMethod", "MagicNumber")

package me.tylerbwong.compose.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun PreferenceScreen(
    preferences: SharedPreferences,
    modifier: Modifier = Modifier,
    content: PreferenceScope.() -> Unit
) {
    CompositionLocalProvider(LocalPreferences provides FlowSharedPreferences(preferences)) {
        LazyColumn(modifier = modifier) {
            content()
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterialApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
internal fun PreferenceScreenPreview() {
    val context = LocalContext.current
    val preferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
    PreferenceScreen(preferences = preferences) {
        PreferenceCategory(header = { Text(text = "Experimental") }) {
            SwitchPreference(
                initialChecked = false,
                key = "syntax_highlighting",
                title = { Text(text = "Syntax Highlighting") },
                summary = { Text(text = "Enables syntax highlighting for supported markdown code blocks.") },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Code,
                        contentDescription = null,
                    )
                },
                singleLineSecondaryText = false,
            )
            SwitchPreference(
                initialChecked = false,
                key = "create_question",
                title = { Text(text = "Create Question") },
                summary = { Text(text = "Enables create question support.") },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = null,
                    )
                },
            )
            SliderPreference(
                initialValue = 25f,
                key = "num_questions",
                onValueChange = {},
                valueRange = 0f..100f,
                steps = 25,
                title = { Text(text = "Number of Questions to Show") },
                valueLabel = { Text(text = it.toInt().toString()) },
                summary = { Text(text = "Specifies the number of questions to show on the home page.") },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Dashboard,
                        contentDescription = null,
                    )
                },
            )
        }
        PreferenceCategory(header = { Text(text = "Debug") }) {
            Preference(
                title = { Text(text = "Inspect Network Traffic") },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Traffic,
                        contentDescription = null,
                    )
                },
            )
            CheckboxPreference(
                initialChecked = true,
                key = "network_debugging",
                title = { Text(text = "Enable Network Debugging") },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.BugReport,
                        contentDescription = null,
                    )
                },
            )
        }
        PreferenceCategory(header = { Text(text = "App") }) {
            ListPreference(
                key = "theme",
                title = { Text(text = "Theme") },
                dialogTitle = { Text(text = "Theme") },
                items = listOf("Light", "Dark", "System default"),
                onConfirm = { _, _ -> },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Brightness2,
                        contentDescription = null,
                    )
                },
            )
            Preference(
                title = { Text(text = "Version") },
                summary = { Text(text = "1.0.0-alpha01") },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                    )
                },
            )
        }
    }
}
