@file:Suppress("FunctionNaming")

package me.tylerbwong.compose.preference

import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.runtime.Composable
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
                icon = Icons.Filled.Code,
            )
            SwitchPreference(
                initialChecked = false,
                onCheckedChange = {},
                title = "Create Question",
                summary = "Enables create question support.",
                icon = Icons.Filled.AddCircle,
            )
        }
        PreferenceCategory("Debug") {
            Preference(
                title = "Inspect Network Traffic",
                icon = Icons.Filled.Traffic,
            )
            CheckboxPreference(
                initialChecked = true,
                onCheckedChange = {},
                title = "Enable Network Debugging",
                icon = Icons.Filled.BugReport,
            )
        }
        PreferenceCategory("App") {
            Preference(
                title = "Theme",
                summary = "System default",
                icon = Icons.Filled.Brightness2,
            )
            Preference(
                title = "Version",
                summary = "1.0.0-alpha01",
                icon = Icons.Filled.Info,
            )
        }
    }
}
