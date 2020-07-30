@file:Suppress("LongMethod")
package me.tylerbwong.stack.ui.questions.create

import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.lifecycle.LiveData
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.gesture.tapGestureFilter
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.input.TextFieldValue
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.height
import androidx.ui.layout.padding
import androidx.ui.layout.width
import androidx.ui.livedata.observeAsState
import androidx.ui.material.Checkbox
import androidx.ui.material.ExtendedFloatingActionButton
import androidx.ui.material.IconButton
import androidx.ui.material.OutlinedTextField
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.ArrowBack
import androidx.ui.material.icons.filled.Delete
import androidx.ui.res.colorResource
import androidx.ui.res.dimensionResource
import androidx.ui.res.stringResource
import androidx.ui.res.vectorResource
import androidx.ui.savedinstancestate.savedInstanceState
import androidx.ui.unit.dp
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
            if (isValidTitle() && isValidBody()) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            text = stringResource(R.string.create),
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                        )
                    },
                    onClick = { createQuestion(title.text, body.text, tags.text, isPreview) },
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
                modifier = Modifier.fillMaxWidth()
                    .height(dimensionResource(R.dimen.body_height)),
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
                activeColor = colorAccent,
                inactiveColor = primaryTextColor
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

    Row(verticalGravity = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            checkedColor = colorAccent,
            uncheckedColor = primaryTextColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            modifier = Modifier.tapGestureFilter { onCheckedChange(!checked) },
            color = primaryTextColor
        )
    }
}
