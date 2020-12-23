package me.tylerbwong.stack.ui.search.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.ui.utils.colorAttribute

@Composable
fun FiltersLayout(
    initialPayload: SearchPayload = SearchPayload.empty(),
    onUpdateFilters: (SearchPayload) -> Unit = {}
) {
    val payload = remember { mutableStateOf(initialPayload) }
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 32.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.h4,
                color = colorAttribute(android.R.attr.textColorPrimary),
                fontWeight = FontWeight.Bold
            )
            SwitchItem(
                text = stringResource(R.string.has_accepted_answer),
                isChecked = payload.value.isAccepted ?: false,
                onCheckedChange = { isChecked ->
                    val newPayload = payload.value.copy(isAccepted = isChecked)
                    payload.value = newPayload
                    onUpdateFilters(newPayload)
                },
                modifier = Modifier.padding(16.dp)
            )
            SwitchItem(
                text = stringResource(R.string.is_closed),
                isChecked = payload.value.isClosed ?: false,
                onCheckedChange = { isChecked ->
                    val newPayload = payload.value.copy(isClosed = isChecked)
                    payload.value = newPayload
                    onUpdateFilters(newPayload)
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun SwitchItem(
    text: String,
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier.then(Modifier.fillMaxWidth()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = colorAttribute(android.R.attr.textColorPrimary)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(R.color.colorAccent)
            )
        )
    }
}

@Suppress("unused")
@Composable
private fun WorkInProgress() {
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        text = "Has at least 0 answers"
    )
    Slider(
        value = 0f,
        onValueChange = {},
        modifier = Modifier.padding(16.dp),
        steps = 50
    )
    TextFieldItem(
        text = stringResource(R.string.title_contains_title),
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        onValueChanged = {}
    )
    TextFieldItem(
        text = stringResource(R.string.body_contains_title),
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        onValueChanged = {}
    )
    TextFieldItem(
        text = stringResource(R.string.tags_title),
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        onValueChanged = {}
    )
    ButtonItem(
        text = stringResource(R.string.apply_filters),
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        onClick = {}
    )
    TextButtonItem(
        text = stringResource(R.string.clear_filters),
        modifier = Modifier.padding(16.dp),
        onClick = {}
    )
}

@Composable
private fun TextFieldItem(
    text: String,
    modifier: Modifier = Modifier,
    onValueChanged: (TextFieldValue) -> Unit
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = TextFieldValue(text = text),
            onValueChange = onValueChanged,
            label = {}
        )
    }
}

@Composable
private fun ButtonItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(text = text)
        }
    }
}

@Composable
private fun TextButtonItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(text = text)
        }
    }
}
