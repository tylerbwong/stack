@file:Suppress("FunctionNaming")

package me.tylerbwong.compose.preference

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.ListItem
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.unit.dp

fun PreferenceScope.CheckboxPreference(
    initialChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    summary: String? = null,
    icon: VectorAsset? = null,
) {
    TwoStatePreference(
        initialChecked = initialChecked,
        onCheckedChange = onCheckedChange,
        title = title,
        summary = summary,
        icon = icon,
        trailing = { checked, onChange ->
            Checkbox(
                checked = checked,
                onCheckedChange = onChange
            )
        }
    )
}

fun PreferenceScope.SwitchPreference(
    initialChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    summary: String? = null,
    icon: VectorAsset? = null,
) {
    TwoStatePreference(
        initialChecked = initialChecked,
        onCheckedChange = onCheckedChange,
        title = title,
        summary = summary,
        icon = icon,
        trailing = { checked, onChange ->
            Switch(
                checked = checked,
                onCheckedChange = onChange
            )
        }
    )
}

fun PreferenceScope.TwoStatePreference(
    initialChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    summary: String? = null,
    icon: VectorAsset? = null,
    trailing: @Composable (initialChecked: Boolean, onCheckedChange: (Boolean) -> Unit) -> Unit
) {
    item {
        var isChecked by savedInstanceState { initialChecked }
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            onClick = { isChecked = !isChecked },
            trailing = { trailing(initialChecked, onCheckedChange) }
        )
    }
}

fun PreferenceScope.Preference(
    title: String,
    summary: String? = null,
    icon: VectorAsset? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit) = {}
) {
    item {
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            onClick = onClick,
            modifier = modifier,
            trailing = trailing
        )
    }
}

@Composable
internal fun PreferenceInternal(
    title: String,
    summary: String?,
    icon: VectorAsset?,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit) = {}
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        icon = icon?.let { { Icon(asset = it, modifier = Modifier.size(42.dp)) } },
        secondaryText = summary?.let { { Text(text = it) } },
        text = { Text(text = title) },
        trailing = trailing
    )
}

//@OptIn(ExperimentalLazyDsl::class)
//@Preview
//@Composable
//fun PreferencePreview() {
//    LazyColumn {
//        Preference(
//            title = "Version",
//            summary = "1.0.0-alpha04",
//            icon = Icons.Filled.Info,
//        )
//    }
//}
//
//@OptIn(ExperimentalLazyDsl::class)
//@Preview
//@Composable
//fun SwitchPreferencePreview() {
//    LazyColumn {
//        SwitchPreference(
//            initialChecked = true,
//            onCheckedChange = {},
//            title = "Version",
//            summary = "1.0.0-alpha04",
//            icon = Icons.Filled.Info,
//        )
//    }
//}
//
//@OptIn(ExperimentalLazyDsl::class)
//@Preview
//@Composable
//fun CheckboxPreferencePreview() {
//    LazyColumn {
//        CheckboxPreference(
//            initialChecked = true,
//            onCheckedChange = {},
//            title = "Version",
//            summary = "1.0.0-alpha04",
//            icon = Icons.Filled.Info,
//        )
//    }
//}
