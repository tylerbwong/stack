@file:Suppress("LongMethod")
package me.tylerbwong.stack.ui.questions.create

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxConstants
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.QuestionDraft

private const val MIN_TITLE_LENGTH = 15
private const val MIN_BODY_LENGTH = 30

@Composable
fun CreateQuestionLayout(
    draftLiveData: LiveData<QuestionDraft>,
    createQuestion: (String, String, String, Boolean) -> Unit = { _, _, _, _ -> },
    saveDraft: (String, String, String) -> Unit = { _, _, _ -> },
    deleteDraft: (Int) -> Unit = { _ -> },
    onBackPressed: () -> Unit = {}
) {
    val viewBackgroundColor = colorResource(R.color.viewBackgroundColor)
    val primaryTextColor = colorResource(R.color.primaryTextColor)
    val iconColor = colorResource(R.color.iconColor)
    val colorAccent = colorResource(R.color.colorAccent)
    val colorError = colorResource(R.color.colorError)

    val draft by draftLiveData.observeAsState(
        initial = QuestionDraft(0, "", 0L, "", "", "")
    )
    var title by savedInstanceState(saver = TextFieldValue.Saver) {
        TextFieldValue(text = draft.title)
    }
    var body by savedInstanceState(saver = TextFieldValue.Saver) {
        TextFieldValue(text = draft.body)
    }
    var tags by savedInstanceState(saver = TextFieldValue.Saver) {
        TextFieldValue(text = draft.tags)
    }
    var isPreview by savedInstanceState { BuildConfig.DEBUG }

    fun isValidTitle() = title.text.isNotBlank() && title.text.length >= MIN_TITLE_LENGTH
    fun isValidBody() = body.text.isNotBlank() && body.text.length >= MIN_BODY_LENGTH
    fun isValidTags() = tags.text.isNotBlank() && tags.text.split(",").isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.create_question),
                        color = primaryTextColor
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        icon = {
                            Icon(
                                asset = Icons.Filled.ArrowBack,
                                tint = iconColor
                            )
                        }
                    )
                },
                actions = {
                    if (listOf(title.text, body.text, tags.text).any { it.isNotBlank() }) {
                        IconButton(
                            onClick = { saveDraft(title.text, body.text, tags.text) },
                            icon = {
                                Icon(
                                    asset = vectorResource(R.drawable.ic_baseline_save),
                                    tint = iconColor
                                )
                            }
                        )
                        IconButton(
                            onClick = {
                                title = TextFieldValue()
                                body = TextFieldValue()
                                tags = TextFieldValue()
                                isPreview = false
                                deleteDraft(draft.id)
                            },
                            icon = {
                                Icon(
                                    asset = Icons.Filled.Delete,
                                    tint = iconColor
                                )
                            }
                        )
                    }
                },
                backgroundColor = viewBackgroundColor
            )
        },
        floatingActionButton = {
            if (isValidTitle() && isValidBody() && isValidTags()) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            text = stringResource(R.string.create),
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                        )
                    },
                    onClick = {
                        createQuestion(
                            title.text,
                            body.text,
                            tags.text,
                            isPreview
                        )
                    },
                    icon = {
                        Icon(
                            asset = vectorResource(R.drawable.ic_send),
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                        )
                    },
                    backgroundColor = colorAccent
                )
            }
        },
        backgroundColor = viewBackgroundColor
    ) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp
            )
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = stringResource(R.string.title)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.body2,
                isErrorValue = !isValidTitle(),
                activeColor = colorAccent,
                inactiveColor = primaryTextColor,
                errorColor = colorError
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text(text = stringResource(R.string.body)) },
                modifier = Modifier.fillMaxWidth().height(dimensionResource(R.dimen.body_height)),
                textStyle = MaterialTheme.typography.body2,
                isErrorValue = !isValidBody(),
                activeColor = colorAccent,
                inactiveColor = primaryTextColor,
                errorColor = colorError
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text(text = stringResource(R.string.tags_title)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.body2,
                isErrorValue = !isValidTags(),
                activeColor = colorAccent,
                inactiveColor = primaryTextColor,
                errorColor = colorError
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (BuildConfig.DEBUG) {
                LabeledCheckbox(
                    label = stringResource(R.string.post_preview),
                    checked = isPreview,
                    onCheckedChange = { isPreview = it }
                )
            }
        }
    }
}

@Composable
private fun LabeledCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val primaryTextColor = colorResource(R.color.primaryTextColor)
    val colorAccent = colorResource(R.color.colorAccent)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxConstants.defaultColors(
                checkedColor = colorAccent,
                uncheckedColor = primaryTextColor
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            modifier = Modifier.tapGestureFilter { onCheckedChange(!checked) },
            color = primaryTextColor
        )
    }
}
