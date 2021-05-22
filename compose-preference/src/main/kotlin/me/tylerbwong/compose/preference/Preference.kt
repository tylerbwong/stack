@file:Suppress("FunctionNaming", "LongMethod", "MagicNumber")

package me.tylerbwong.compose.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.ExperimentalCoroutinesApi

private val DefaultIconSize = 24.dp
private val DefaultIconPadding = 8.dp

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
fun PreferenceScope.ListPreference(
    key: String,
    title: @Composable () -> Unit,
    dialogTitle: @Composable () -> Unit,
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
            .collectAsState(initial = preferences.sharedPreferences.getInt(key, selectedItemIndex))
        var isAlertDialogVisible by rememberSaveable { mutableStateOf(false) }
        val onClick: (Int) -> Unit = {
            preferences.sharedPreferences.edit().putInt(key, it).apply()
            onConfirm(items[itemIndex], itemIndex)
            isAlertDialogVisible = false
        }

        PreferenceInternal(
            title = title,
            summary = { Text(text = items[itemIndex]) },
            icon = icon,
            singleLineSecondaryText = singleLineSecondaryText,
            onClick = { isAlertDialogVisible = true },
        )

        if (isAlertDialogVisible) {
            Dialog(
                onDismissRequest = { isAlertDialogVisible = false },
                content = {
                    Card(shape = RoundedCornerShape(8.dp)) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ProvideTextStyle(
                                value = TextStyle(color = MaterialTheme.colors.onSurface) +
                                        MaterialTheme.typography.h6
                            ) {
                                Box(
                                    modifier = Modifier.padding(
                                        start = 24.dp,
                                        top = 20.dp,
                                        end = 24.dp,
                                        bottom = 8.dp,
                                    ),
                                ) { dialogTitle() }
                            }
                            items.forEachIndexed { index, item ->
                                val interactionSource = remember { MutableInteractionSource() }
                                Row(
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = rememberRipple(),
                                            onClick = { onClick(index) },
                                        )
                                        .padding(
                                            horizontal = 24.dp,
                                            vertical = 12.dp,
                                        )
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RadioButton(
                                        selected = index == itemIndex,
                                        onClick = { onClick(index) },
                                    )
                                    Spacer(modifier = Modifier.width(24.dp))
                                    Text(
                                        text = item,
                                        color = MaterialTheme.colors.onSurface,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                },
            )
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
fun PreferenceScope.SliderPreference(
    initialValue: Float,
    key: String,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    title: @Composable () -> Unit,
    valueLabel: (@Composable (value: Float) -> Unit)? = null,
    summary: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    item {
        val preferences = LocalPreferences.current
        val currentValue by preferences.getFloat(key, initialValue)
            .asFlow()
            .collectAsState(initial = preferences.sharedPreferences.getFloat(key, initialValue))

        ListItem(
            icon = icon?.let {
                {
                    Box(
                        modifier = Modifier
                            .padding(DefaultIconPadding)
                            .size(DefaultIconSize),
                    ) { it() }
                }
            },
            secondaryText = {
                Column {
                    summary?.let {
                        ProvideTextStyle(
                            value = TextStyle(
                                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
                            )
                        ) { it() }
                    }
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
            text = {
                ProvideTextStyle(value = TextStyle(color = MaterialTheme.colors.onBackground)) {
                    title()
                }
            },
        )
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
fun PreferenceScope.CheckboxPreference(
    initialChecked: Boolean,
    key: String,
    title: @Composable () -> Unit,
    onCheckedChange: (Boolean) -> Unit = {},
    summary: (@Composable () -> Unit)? = null,
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

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
fun PreferenceScope.SwitchPreference(
    initialChecked: Boolean,
    key: String,
    title: @Composable () -> Unit,
    onCheckedChange: (Boolean) -> Unit = {},
    summary: (@Composable () -> Unit)? = null,
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

@ExperimentalMaterialApi
fun PreferenceScope.Preference(
    title: @Composable () -> Unit,
    summary: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
    onClick: () -> Unit = {},
) {
    item {
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            singleLineSecondaryText = singleLineSecondaryText,
            onClick = onClick,
        )
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
internal fun PreferenceScope.TwoStatePreference(
    initialChecked: Boolean,
    key: String,
    onCheckedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    summary: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
    trailing: @Composable (checked: Boolean, toggle: (Boolean) -> Unit) -> Unit,
) {
    item {
        val preferences = LocalPreferences.current
        val isChecked by preferences.getBoolean(key, initialChecked)
            .asFlow()
            .collectAsState(initial = preferences.sharedPreferences.getBoolean(key, initialChecked))
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
            trailing = { trailing(isChecked, toggle) },
        )
    }
}

@ExperimentalMaterialApi
@Composable
internal fun PreferenceInternal(
    title: @Composable () -> Unit,
    summary: (@Composable () -> Unit)?,
    icon: (@Composable () -> Unit)?,
    modifier: Modifier = Modifier,
    singleLineSecondaryText: Boolean = true,
    onClick: () -> Unit = {},
    trailing: (@Composable () -> Unit) = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = rememberRipple(),
            onClick = onClick
        ),
        icon = {
            Box(
                modifier = Modifier
                    .padding(DefaultIconPadding)
                    .size(DefaultIconSize),
            ) { icon?.invoke() }
        },
        secondaryText = summary?.let {
            {
                ProvideTextStyle(
                    value = TextStyle(color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f))
                ) { it() }
            }
        },
        singleLineSecondaryText = singleLineSecondaryText,
        text = {
            ProvideTextStyle(value = TextStyle(color = MaterialTheme.colors.onBackground)) {
                title()
            }
        },
        trailing = trailing,
    )
}
