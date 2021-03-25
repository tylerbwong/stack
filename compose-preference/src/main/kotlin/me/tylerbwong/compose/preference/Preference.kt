@file:Suppress("FunctionNaming", "LongMethod", "MagicNumber")

package me.tylerbwong.compose.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
fun PreferenceScope.ListPreference(
    key: String,
    title: String,
    dialogTitle: String,
    items: List<String>,
    onConfirm: (String, Int) -> Unit,
    selectedItemIndex: Int = 0,
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
) {
    require(selectedItemIndex in 0..items.size) {
        "Selected item index is out of bounds"
    }
    item {
        val preferences = LocalPreferences.current
        val itemIndex by preferences.getInt(key, selectedItemIndex)
            .asFlow()
            .collectAsState(
                initial = preferences.sharedPreferences.getInt(key, selectedItemIndex),
                context = Dispatchers.IO,
            )
        var isAlertDialogVisible by rememberSaveable { mutableStateOf(false) }
        var summaryState by rememberSaveable { mutableStateOf(items[itemIndex]) }
        val onClick: (Int) -> Unit = {
            preferences.sharedPreferences
                .edit()
                .putInt(key, it)
                .apply()
        }
        PreferenceInternal(
            title = title,
            summary = summaryState,
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
                            val interactionSource = remember { MutableInteractionSource() }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        onClick = { onClick(index) },
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = index == itemIndex,
                                    onClick = { onClick(index) },
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
                            onClick(itemIndex)
                            onConfirm(items[itemIndex], itemIndex)
                            summaryState = items[itemIndex]
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class)
fun PreferenceScope.SliderPreference(
    initialValue: Float,
    key: String,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    title: String,
    valueLabel: (@Composable (value: Float) -> Unit)? = null,
    summary: String? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    item {
        val preferences = LocalPreferences.current
        val currentValue by preferences.getFloat(key, initialValue)
            .asFlow()
            .collectAsState(
                initial = preferences.sharedPreferences.getFloat(key, initialValue),
                context = Dispatchers.IO,
            )
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
                                preferences.sharedPreferences.edit().putFloat(key, it).apply()
                                onValueChange(it)
                            },
                            valueRange = valueRange,
                            steps = steps,
                        )
                    }
                }
            },
            text = { Text(text = title, color = MaterialTheme.colors.onBackground) },
        )
    }
}

fun PreferenceScope.CheckboxPreference(
    initialChecked: Boolean,
    key: String,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    summary: String? = null,
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
) {
    TwoStatePreference(
        initialChecked = initialChecked,
        key = key,
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
    key: String,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    summary: String? = null,
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
) {
    TwoStatePreference(
        initialChecked = initialChecked,
        key = key,
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
            content = trailing,
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
internal fun PreferenceScope.TwoStatePreference(
    initialChecked: Boolean,
    key: String,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    summary: String? = null,
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
    trailing: @Composable (checked: Boolean, toggle: (Boolean) -> Unit) -> Unit,
) {
    item {
        val preferences = LocalPreferences.current
        val isChecked by preferences.getBoolean(key, initialChecked)
            .asFlow()
            .collectAsState(
                initial = preferences.sharedPreferences.getBoolean(key, initialChecked),
                context = Dispatchers.IO,
            )
        val toggle: (Boolean) -> Unit = {
            preferences.sharedPreferences.edit().putBoolean(key, it).apply()
            onCheckedChange(it)
        }
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            singleLineSecondaryText = singleLineSecondaryText,
            onClick = { toggle(!isChecked) },
            content = { trailing(isChecked, toggle) },
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PreferenceInternal(
    title: String,
    summary: String?,
    icon: (@Composable () -> Unit)?,
    modifier: Modifier = Modifier,
    singleLineSecondaryText: Boolean = true,
    onClick: () -> Unit = {},
    content: (@Composable () -> Unit) = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    ListItem(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = rememberRipple(),
            onClick = onClick
        ),
        icon = icon,
        secondaryText = summary?.let {
            {
                Text(
                    text = it,
                    color = MaterialTheme.colors.onBackground
                )
            }
        },
        singleLineSecondaryText = singleLineSecondaryText,
        text = { Text(text = title, color = MaterialTheme.colors.onBackground) },
        trailing = content,
    )
}
