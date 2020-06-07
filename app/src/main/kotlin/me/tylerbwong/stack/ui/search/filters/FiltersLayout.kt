@file:Suppress("FunctionNaming") // TODO Remove when detekt supports ignoreAnnotated option
package me.tylerbwong.stack.ui.search.filters

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Slider
import androidx.ui.material.Switch
import androidx.ui.material.TextButton
import androidx.ui.res.colorResource
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.ui.utils.colorAttribute

@Preview
@Composable
fun FiltersLayout(
    initialPayload: SearchPayload = SearchPayload.empty(),
    onUpdateFilters: (SearchPayload) -> Unit = {}
) {
    val payload = state { initialPayload }
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.h4,
                color = colorAttribute(android.R.attr.textColorPrimary)
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
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.plus(Modifier.fillMaxWidth()),
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
        TextField(
            value = TextFieldValue(text = text),
            onValueChange = onValueChanged
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
