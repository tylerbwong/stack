@file:Suppress("FunctionNaming", "MagicNumber")

package me.tylerbwong.compose.preference

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.TextButton
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun PreferenceScope.ListPreference(
    title: String,
    dialogTitle: String,
    items: List<String>,
    onConfirm: (String, Int) -> Unit,
    selectedItemIndex: Int = 0,
    summary: String? = null,
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
) {
    item {
        var itemIndex by savedInstanceState { selectedItemIndex }
        var isAlertDialogVisible by savedInstanceState { false }
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            singleLineSecondaryText = singleLineSecondaryText,
            onClick = { isAlertDialogVisible = true },
        )

        if (isAlertDialogVisible) {
            AlertDialog(
                onDismissRequest = { isAlertDialogVisible = false },
                title = {
                    Text(
                        text = dialogTitle,
                        style = MaterialTheme.typography.h6,
                    )
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        items.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = { itemIndex = index }, indication = null),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = index == itemIndex,
                                    onClick = { itemIndex = index },
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = item, fontSize = 18.sp)
                            }
                            if (index != items.lastIndex) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirm(items[itemIndex], itemIndex)
                            isAlertDialogVisible = false
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isAlertDialogVisible = false }) {
                        Text(text = "Dismiss")
                    }
                },
                shape = RoundedCornerShape(8.dp),
            )
        }
    }
}

fun PreferenceScope.SliderPreference(
    initialValue: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    title: String,
    valueLabel: (@Composable (value: Float) -> Unit)? = null,
    summary: String? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    item {
        var currentValue by savedInstanceState { initialValue }
        ListItem(
            icon = icon,
            secondaryText = {
                Column {
                    summary?.let { Text(text = it) }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        valueLabel?.let {
                            it(currentValue)
                            Spacer(modifier = Modifier.width(2.dp))
                        }
                        Slider(
                            value = currentValue,
                            onValueChange = {
                                onValueChange(it)
                                currentValue = it
                            },
                            valueRange = valueRange,
                            steps = steps,
                        )
                    }
                }
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
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
) {
    TwoStatePreference(
        initialChecked = initialChecked,
        onCheckedChange = onCheckedChange,
        title = title,
        summary = summary,
        icon = icon,
        singleLineSecondaryText = singleLineSecondaryText,
        trailing = { checked, toggle ->
            Checkbox(
                checked = checked,
                onCheckedChange = toggle,
            )
        },
    )
}

fun PreferenceScope.SwitchPreference(
    initialChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    summary: String? = null,
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
) {
    TwoStatePreference(
        initialChecked = initialChecked,
        onCheckedChange = onCheckedChange,
        title = title,
        summary = summary,
        icon = icon,
        singleLineSecondaryText = singleLineSecondaryText,
        trailing = { checked, toggle ->
            Switch(
                checked = checked,
                onCheckedChange = toggle,
            )
        },
    )
}

fun PreferenceScope.Preference(
    title: String,
    summary: String? = null,
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
    onClick: () -> Unit = {},
    trailing: (@Composable () -> Unit) = {},
) {
    item {
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            singleLineSecondaryText = singleLineSecondaryText,
            onClick = onClick,
            trailing = trailing,
        )
    }
}

internal fun PreferenceScope.TwoStatePreference(
    initialChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    summary: String? = null,
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
    trailing: @Composable (checked: Boolean, toggle: (Boolean) -> Unit) -> Unit,
) {
    item {
        var isChecked by savedInstanceState { initialChecked }
        val toggle: (Boolean) -> Unit = {
            onCheckedChange(it)
            isChecked = it
        }
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            singleLineSecondaryText = singleLineSecondaryText,
            onClick = { toggle(!isChecked) },
            trailing = { trailing(isChecked, toggle) },
        )
    }
}

@Composable
internal fun PreferenceInternal(
    title: String,
    summary: String?,
    icon: (@Composable () -> Unit)?,
    singleLineSecondaryText: Boolean = true,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit) = {},
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick, indication = RippleIndication()),
        icon = icon,
        secondaryText = summary?.let { { Text(text = it) } },
        singleLineSecondaryText = singleLineSecondaryText,
        text = { Text(text = title) },
        trailing = trailing,
    )
}
