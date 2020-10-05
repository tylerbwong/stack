@file:Suppress("FunctionNaming")

package me.tylerbwong.compose.preference

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.ListItem
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.unit.dp

fun PreferenceScope.SeekbarPreference(
    initialValue: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    title: String,
    summary: String? = null,
    icon: VectorAsset? = null,
) {
    item {
        ListItem(
            modifier = Modifier.clickable(onClick = {}),
            icon = icon?.let { { Icon(asset = it, modifier = Modifier.size(42.dp)) } },
            secondaryText = {
                summary?.let { Text(text = it) }
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = initialValue,
                    onValueChange = onValueChange,
                    valueRange = valueRange,
                    steps = steps,
                )
            },
            text = { Text(text = title) },
        )
    }
}

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
                onCheckedChange = onChange,
            )
        },
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
                onCheckedChange = onChange,
            )
        },
    )
}

fun PreferenceScope.TwoStatePreference(
    initialChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    summary: String? = null,
    icon: VectorAsset? = null,
    trailing: @Composable (initialChecked: Boolean, onCheckedChange: (Boolean) -> Unit) -> Unit,
) {
    item {
        var isChecked by savedInstanceState { initialChecked }
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            onClick = { isChecked = !isChecked },
            trailing = { trailing(initialChecked, onCheckedChange) },
        )
    }
}

fun PreferenceScope.Preference(
    title: String,
    summary: String? = null,
    icon: VectorAsset? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit) = {},
) {
    item {
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            onClick = onClick,
            modifier = modifier,
            trailing = trailing,
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
    trailing: (@Composable () -> Unit) = {},
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        icon = icon?.let { { Icon(asset = it, modifier = Modifier.size(42.dp)) } },
        secondaryText = summary?.let { { Text(text = it) } },
        text = { Text(text = title) },
        trailing = trailing,
    )
}
