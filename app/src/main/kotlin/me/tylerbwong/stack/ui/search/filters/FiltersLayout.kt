package me.tylerbwong.stack.ui.search.filters

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.ui.utils.colorAttribute
import me.tylerbwong.stack.ui.utils.quantityResource

@Composable
fun FiltersLayout(
    initialPayload: SearchPayload,
    onUpdateFilters: (SearchPayload) -> Unit,
    onApplyClicked: () -> Unit,
    onClearClicked: () -> Unit
) {
    val colorAccent = colorResource(R.color.colorAccent)
    var isAccepted by remember { mutableStateOf(initialPayload.isAccepted) }
    var isClosed by remember { mutableStateOf(initialPayload.isClosed) }
    var sliderValue by remember {
        mutableStateOf(initialPayload.minNumAnswers?.toFloat())
    }
    var titleContainsValue by remember {
        mutableStateOf(initialPayload.titleContains?.let { TextFieldValue(it) })
    }
    var bodyContainsValue by remember {
        mutableStateOf(initialPayload.bodyContains?.let { TextFieldValue(it) })
    }
    var tagsValue by remember {
        mutableStateOf(initialPayload.tags?.joinToString(",")?.let { TextFieldValue(it) })
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = stringResource(R.string.filters),
            color = colorAttribute(android.R.attr.textColorPrimary),
            style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.height(16.dp))
        SwitchItem(
            text = stringResource(R.string.has_accepted_answer),
            isChecked = isAccepted ?: false,
            onCheckedChange = { isChecked ->
                isAccepted = isChecked
                onUpdateFilters(initialPayload.copy(isAccepted = isAccepted))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SwitchItem(
            text = stringResource(R.string.is_closed),
            isChecked = isClosed ?: false,
            onCheckedChange = { isChecked ->
                isClosed = isChecked
                onUpdateFilters(initialPayload.copy(isClosed = isClosed))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = quantityResource(R.plurals.has_min_answers, sliderValue?.toInt() ?: 0),
            color = colorAttribute(android.R.attr.textColorPrimary),
            style = MaterialTheme.typography.caption
        )
        Slider(
            value = sliderValue ?: 0f,
            onValueChange = {
                sliderValue = it
                onUpdateFilters(initialPayload.copy(minNumAnswers = it.toInt()))
            },
            valueRange = 0f..50f,
            thumbColor = colorAccent,
            activeTrackColor = colorAccent,
            inactiveTrackColor = colorAccent,
            activeTickColor = colorAccent,
            inactiveTickColor = colorAccent
        )
        Spacer(modifier = Modifier.height(16.dp))
        FilterTextField(
            textValue = titleContainsValue ?: TextFieldValue(),
            onValueChanged = {
                titleContainsValue = it
                onUpdateFilters(initialPayload.copy(titleContains = it.text))
            },
            label = {
                Text(
                    text = stringResource(R.string.title_contains_title),
                    color = colorAccent
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        FilterTextField(
            textValue = bodyContainsValue ?: TextFieldValue(),
            onValueChanged = {
                bodyContainsValue = it
                onUpdateFilters(initialPayload.copy(bodyContains = it.text))
            },
            label = {
                Text(
                    text = stringResource(R.string.body_contains_title),
                    color = colorAccent
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        FilterTextField(
            textValue = tagsValue ?: TextFieldValue(),
            onValueChanged = {
                tagsValue = it
                onUpdateFilters(
                    initialPayload.copy(
                        tags = it.text.split(",").map { tag -> tag.trim() }
                    )
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.tags_title),
                    color = colorAccent
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onApplyClicked() },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = colorAccent,
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.apply_filters),
                color = colorResource(R.color.viewBackgroundColor)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = { onClearClicked() },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            contentColor = colorAccent
        ) {
            Text(text = stringResource(R.string.clear_filters))
        }
    }
}

@Composable
private fun SwitchItem(
    text: String,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = colorAttribute(android.R.attr.textColorPrimary)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            color = colorResource(R.color.colorAccent)
        )
    }
}

@Composable
private fun FilterTextField(
    textValue: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
    label: @Composable () -> Unit
) {
    OutlinedTextField(
        value = textValue,
        onValueChange = {
            // Single line hack
            if (!it.text.contains("\n")) {
                onValueChanged(it)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.body2,
        label = label,
        activeColor = colorResource(R.color.colorAccent),
        inactiveColor = colorResource(R.color.iconColor)
    )
}
