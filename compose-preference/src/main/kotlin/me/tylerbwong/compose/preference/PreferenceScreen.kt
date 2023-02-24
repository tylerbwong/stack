@file:Suppress("FunctionNaming", "LongMethod", "MagicNumber")

package me.tylerbwong.compose.preference

import android.content.Context
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PreferenceScreen(
    modifier: Modifier = Modifier,
    content: PreferenceScope.() -> Unit
) {
    LazyColumn(modifier = modifier) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
internal fun PreferenceScreenPreview() {
    val context = LocalContext.current
    val preferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
    var syntaxPref by remember {
        mutableStateOf(preferences.getBoolean("syntax_highlighting", false))
    }
    var createPref by remember {
        mutableStateOf(preferences.getBoolean("ask_question", false))
    }
    var sliderPref by remember {
        mutableStateOf(preferences.getFloat("num_questions", 0f))
    }
    var networkPref by remember {
        mutableStateOf(preferences.getBoolean("network_debugging", false))
    }
    var themePref by remember {
        mutableStateOf(preferences.getInt("theme", 0))
    }
    PreferenceScreen {
        repeat(100) {
            PreferenceCategory(header = { Text(text = "Experimental") }) {
                SwitchPreference(
                    checked = syntaxPref,
                    title = { Text(text = "Syntax Highlighting") },
                    summary = { Text(text = "Enables syntax highlighting for supported markdown code blocks.") },
                    onCheckedChange = {
                        preferences.edit().putBoolean("syntax_highlighting", it).apply()
                        syntaxPref = it
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Code,
                            contentDescription = null,
                        )
                    },
                    singleLineSecondaryText = false,
                )
                SwitchPreference(
                    checked = createPref,
                    title = { Text(text = "Ask Question") },
                    summary = { Text(text = "Enables ask question support.") },
                    onCheckedChange = {
                        preferences.edit().putBoolean("ask_question", it).apply()
                        createPref = it
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = null,
                        )
                    },
                )
                SliderPreference(
                    value = sliderPref,
                    onValueChange = {
                        preferences.edit().putFloat("num_questions", it).apply()
                        sliderPref = it
                    },
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
                    checked = networkPref,
                    title = { Text(text = "Enable Network Debugging") },
                    onCheckedChange = {
                        preferences.edit().putBoolean("network_debugging", it).apply()
                        networkPref = it
                    },
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
                    selectedIndex = themePref,
                    title = { Text(text = "Theme") },
                    dialogTitle = { Text(text = "Theme") },
                    items = listOf("Light", "Dark", "System default"),
                    onConfirm = { _, index ->
                        preferences.edit().putInt("theme", index).apply()
                        themePref = index
                    },
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
}
