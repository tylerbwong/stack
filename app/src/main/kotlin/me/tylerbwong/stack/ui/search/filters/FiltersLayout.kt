package me.tylerbwong.stack.ui.search.filters

import androidx.compose.foundation.ProvideTextStyle
import androidx.compose.foundation.Text
import androidx.compose.foundation.currentTextStyle
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focusObserver
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.colorAttribute
import me.tylerbwong.stack.ui.utils.quantityResource

@Composable
fun FiltersLayout(dismissDialog: () -> Unit) {
    val colorAccent = colorResource(R.color.colorAccent)
    val viewModel = viewModel<FilterViewModel>()
    val initialPayload = viewModel.currentPayload
    var isAccepted by savedInstanceState { initialPayload.isAccepted }
    var isClosed by savedInstanceState { initialPayload.isClosed }
    var sliderValue by savedInstanceState { initialPayload.minNumAnswers?.toFloat() }
    var titleContainsValue by savedInstanceState { initialPayload.titleContains }
    var bodyContainsValue by savedInstanceState { initialPayload.bodyContains }
    var tagsValue by savedInstanceState { initialPayload.tags?.joinToString(",") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                viewModel.currentPayload = initialPayload.copy(isAccepted = isChecked)
                isAccepted = isChecked
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SwitchItem(
            text = stringResource(R.string.is_closed),
            isChecked = isClosed ?: false,
            onCheckedChange = { isChecked ->
                viewModel.currentPayload = initialPayload.copy(isClosed = isChecked)
                isClosed = isChecked
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
                viewModel.currentPayload = initialPayload.copy(minNumAnswers = it.toInt())
                sliderValue = it
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
            textValue = titleContainsValue,
            onValueChanged = {
                viewModel.currentPayload = initialPayload.copy(titleContains = it)
                titleContainsValue = it
            },
            label = { Text(text = stringResource(R.string.title_contains_title)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        FilterTextField(
            textValue = bodyContainsValue,
            onValueChanged = {
                viewModel.currentPayload = initialPayload.copy(bodyContains = it)
                bodyContainsValue = it
            },
            label = { Text(text = stringResource(R.string.body_contains_title)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        FilterTextField(
            textValue = tagsValue,
            onValueChanged = {
                val splitValues = it.split(",").map { tag -> tag.trim() }
                viewModel.currentPayload = initialPayload.copy(tags = splitValues)
                tagsValue = it
            },
            label = { Text(text = stringResource(R.string.tags_title)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.applyFilters()
                dismissDialog()
            },
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
            onClick = {
                viewModel.clearFilters()
                dismissDialog()
            },
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

@OptIn(ExperimentalFocus::class)
@Composable
private fun FilterTextField(
    textValue: String?,
    onValueChanged: (String) -> Unit,
    label: @Composable () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = textValue ?: "",
        onValueChange = {
            // Single line hack
            if (!it.contains("\n")) {
                onValueChanged(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusObserver { isFocused = it == FocusState.Active },
        textStyle = MaterialTheme.typography.body2,
        label = {
            if (isFocused) {
                val accentTextStyle = currentTextStyle()
                    .copy(color = colorResource(R.color.colorAccent))
                ProvideTextStyle(value = accentTextStyle) { label() }
            } else {
                label()
            }
        },
        activeColor = colorResource(R.color.colorAccent),
        inactiveColor = colorResource(R.color.iconColor)
    )
}
