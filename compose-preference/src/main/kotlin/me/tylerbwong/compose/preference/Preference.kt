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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

private val DefaultIconSize = 24.dp
private val DefaultIconPadding = 8.dp

@ExperimentalMaterial3Api
fun PreferenceScope.ListPreference(
    selectedIndex: Int,
    title: @Composable () -> Unit,
    dialogTitle: @Composable () -> Unit,
    items: List<String>,
    onConfirm: (String, Int) -> Unit,
    icon: (@Composable () -> Unit)? = null,
) {
    require(selectedIndex in 0..items.size) {
        "Selected index is out of bounds"
    }
    item {
        var isAlertDialogVisible by rememberSaveable { mutableStateOf(false) }
        val onClick: (Int) -> Unit = {
            onConfirm(items[it], it)
            isAlertDialogVisible = false
        }

        PreferenceInternal(
            title = title,
            summary = { Text(text = items[selectedIndex]) },
            icon = icon,
            onClick = { isAlertDialogVisible = true },
        )

        if (isAlertDialogVisible) {
            Dialog(
                onDismissRequest = { isAlertDialogVisible = false },
                content = {
                    Card(shape = RoundedCornerShape(8.dp)) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ProvideTextStyle(
                                value = TextStyle(color = MaterialTheme.colorScheme.onSurface) +
                                        MaterialTheme.typography.headlineSmall
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
                                        .padding(horizontal = 24.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RadioButton(
                                        selected = index == selectedIndex,
                                        onClick = { onClick(index) },
                                    )
                                    Spacer(modifier = Modifier.width(24.dp))
                                    Text(
                                        text = item,
                                        color = MaterialTheme.colorScheme.onSurface,
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

@ExperimentalMaterial3Api
fun PreferenceScope.SliderPreference(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    title: @Composable () -> Unit,
    valueLabel: (@Composable (value: Float) -> Unit)? = null,
    summary: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    item {
        ListItem(
            headlineContent = {
                ProvideTextStyle(value = TextStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    title()
                }
            },
            supportingContent = {
                Column {
                    summary?.let {
                        ProvideTextStyle(
                            value = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        ) { it() }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        valueLabel?.let {
                            it(value)
                            Spacer(modifier = Modifier.width(2.dp))
                        }
                        Slider(
                            value = value,
                            onValueChange = onValueChange,
                            valueRange = valueRange,
                            steps = steps,
                        )
                    }
                }
            },
            leadingContent = icon?.let {
                {
                    Box(
                        modifier = Modifier
                            .padding(DefaultIconPadding)
                            .size(DefaultIconSize),
                    ) { it() }
                }
            },
        )
    }
}

@ExperimentalMaterial3Api
fun PreferenceScope.CheckboxPreference(
    checked: Boolean,
    title: @Composable () -> Unit,
    onCheckedChange: (Boolean) -> Unit = {},
    summary: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    TwoStatePreference(
        checked = checked,
        onCheckedChange = onCheckedChange,
        title = title,
        summary = summary,
        icon = icon,
        trailing = { isChecked, toggle ->
            Checkbox(
                checked = isChecked,
                onCheckedChange = toggle,
            )
        },
    )
}

@ExperimentalMaterial3Api
fun PreferenceScope.SwitchPreference(
    checked: Boolean,
    title: @Composable () -> Unit,
    onCheckedChange: (Boolean) -> Unit = {},
    summary: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    TwoStatePreference(
        checked = checked,
        onCheckedChange = onCheckedChange,
        title = title,
        summary = summary,
        icon = icon,
        trailing = { isChecked, toggle ->
            Switch(
                checked = isChecked,
                onCheckedChange = toggle,
            )
        },
    )
}

@ExperimentalMaterial3Api
fun PreferenceScope.Preference(
    title: @Composable () -> Unit,
    summary: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    item {
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            onClick = onClick,
        )
    }
}

@ExperimentalMaterial3Api
internal fun PreferenceScope.TwoStatePreference(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    summary: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    trailing: @Composable (Boolean, (Boolean) -> Unit) -> Unit,
) {
    item {
        PreferenceInternal(
            title = title,
            summary = summary,
            icon = icon,
            onClick = { onCheckedChange(!checked) },
            trailing = { trailing(checked, onCheckedChange) },
        )
    }
}

@ExperimentalMaterial3Api
@Composable
internal fun PreferenceInternal(
    title: @Composable () -> Unit,
    summary: (@Composable () -> Unit)?,
    icon: (@Composable () -> Unit)?,
    modifier: Modifier = Modifier,
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
        headlineContent = {
            ProvideTextStyle(value = TextStyle(color = MaterialTheme.colorScheme.onBackground)) {
                title()
            }
        },
        supportingContent = summary?.let {
            {
                ProvideTextStyle(
                    value = TextStyle(
                        color = MaterialTheme
                            .colorScheme
                            .onBackground.copy(alpha = 0.5f))
                ) { it() }
            }
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .padding(DefaultIconPadding)
                    .size(DefaultIconSize),
            ) { icon?.invoke() }
        },
        trailingContent = trailing,
    )
}
