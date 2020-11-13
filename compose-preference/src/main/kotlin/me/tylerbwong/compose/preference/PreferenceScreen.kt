@file:Suppress("FunctionNaming", "MagicNumber")

package me.tylerbwong.compose.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.Providers
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun PreferenceScreen(preferences: SharedPreferences, content: PreferenceScope.() -> Unit) {
    Providers(PreferenceAmbient provides preferences) {
        LazyColumn {
            content()
        }
    }
}

@Preview
@Composable
fun PreferenceScreenPreview() {
    val context = ContextAmbient.current
    val preferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
    PreferenceScreen(preferences = preferences) {
        PreferenceCategory("Experimental") {
            SwitchPreference(
                initialChecked = false,
                key = "syntax_highlighting",
                onCheckedChange = {},
                title = "Syntax Highlighting",
                summary = "Enables syntax highlighting for supported markdown code blocks.",
                icon = { Icon(asset = Icons.Filled.Code, modifier = Modifier.size(42.dp)) },
                singleLineSecondaryText = false,
            )
            SwitchPreference(
                initialChecked = false,
                key = "create_question",
                onCheckedChange = {},
                title = "Create Question",
                summary = "Enables create question support.",
                icon = { Icon(asset = Icons.Filled.AddCircle, modifier = Modifier.size(42.dp)) },
            )
            SliderPreference(
                initialValue = 25f,
                key = "num_questions",
                onValueChange = {},
                valueRange = 0f..100f,
                steps = 25,
                title = "Number of Questions to Show",
                valueLabel = { Text(text = it.toInt().toString()) },
                summary = "Specifies the number of questions to show on the home page.",
                icon = { Icon(asset = Icons.Filled.Dashboard, modifier = Modifier.size(42.dp)) },
            )
        }
        PreferenceCategory("Debug") {
            Preference(
                title = "Inspect Network Traffic",
                icon = { Icon(asset = Icons.Filled.Traffic, Modifier.size(42.dp)) },
            )
            CheckboxPreference(
                initialChecked = true,
                key = "network_debugging",
                onCheckedChange = {},
                title = "Enable Network Debugging",
                icon = { Icon(asset = Icons.Filled.BugReport, Modifier.size(42.dp)) },
            )
        }
        PreferenceCategory("App") {
            ListPreference(
                key = "theme",
                title = "Theme",
                dialogTitle = "Choose theme",
                items = listOf("Light", "Dark", "System default"),
                onConfirm = { _, _ -> },
                icon = { Icon(asset = Icons.Filled.Brightness2, modifier = Modifier.size(42.dp)) },
            )
            Preference(
                title = "Version",
                summary = "1.0.0-alpha01",
                icon = { Icon(asset = Icons.Filled.Info, modifier = Modifier.size(42.dp)) },
            )
        }
    }
}
