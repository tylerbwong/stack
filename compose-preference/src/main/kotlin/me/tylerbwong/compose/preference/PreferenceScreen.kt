@file:Suppress("FunctionNaming", "MagicNumber")

package me.tylerbwong.compose.preference

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@OptIn(ExperimentalLazyDsl::class)
@Composable
fun PreferenceScreen(content: PreferenceScope.() -> Unit) {
    LazyColumn {
        content()
    }
}

@Preview
@Composable
fun PreferenceScreenPreview() {
    PreferenceScreen {
        PreferenceCategory("Experimental") {
            SwitchPreference(
                initialChecked = false,
                onCheckedChange = {},
                title = "Syntax Highlighting",
                summary = "Enables syntax highlighting for supported markdown code blocks.",
                icon = { Icon(asset = Icons.Filled.Code, modifier = Modifier.size(42.dp)) },
                singleLineSecondaryText = false,
            )
            SwitchPreference(
                initialChecked = false,
                onCheckedChange = {},
                title = "Create Question",
                summary = "Enables create question support.",
                icon = { Icon(asset = Icons.Filled.AddCircle, modifier = Modifier.size(42.dp)) },
            )
            SliderPreference(
                initialValue = 25f,
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
                onCheckedChange = {},
                title = "Enable Network Debugging",
                icon = { Icon(asset = Icons.Filled.BugReport, Modifier.size(42.dp)) },
            )
        }
        PreferenceCategory("App") {
            ListPreference(
                title = "Theme",
                dialogTitle = "Choose theme",
                items = listOf("Light", "Dark", "System default"),
                onConfirm = { _, _ -> },
                summary = "System default",
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
