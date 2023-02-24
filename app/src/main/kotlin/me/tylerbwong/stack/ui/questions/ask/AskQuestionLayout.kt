@file:Suppress("LongMethod")

package me.tylerbwong.stack.ui.questions.ask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.QuestionDraft
import me.tylerbwong.stack.ui.utils.compose.LabeledCheckbox
import me.tylerbwong.stack.ui.utils.compose.StackTheme

private const val MIN_TITLE_LENGTH = 15
private const val MIN_BODY_LENGTH = 30

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun AskQuestionLayout2(onFinish: () -> Unit) {
    val pagerState = rememberPagerState()
    StackTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onFinish) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {}
                )
            },
            bottomBar = { BottomNavigationBar(state = pagerState) }
        ) {
            HorizontalPager(
                count = 5,
                modifier = Modifier.padding(it),
                state = pagerState,
                userScrollEnabled = false,
            ) { page ->
                when (page) {
                    0 -> TitleLayoutPreview()
                    1 -> DetailsLayoutPreview()
                    2 -> ExpandLayoutPreview()
                    3 -> TagsLayoutPreview()
                    4 -> DuplicateQuestionLayoutPreview()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BottomNavigationBar(state: PagerState) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            onClick = {
                scope.launch {
                    state.animateScrollToPage((state.currentPage - 1).coerceAtLeast(0))
                }
            },
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = "Back")
        }
        Button(
            onClick = {
                scope.launch {
                    state.animateScrollToPage((state.currentPage + 1).coerceAtMost(state.pageCount - 1))
                }
            },
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = "Next")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskQuestionLayout(
    draftLiveData: LiveData<QuestionDraft>,
    askQuestion: (String, String, String, Boolean) -> Unit = { _, _, _, _ -> },
    saveDraft: (String, String, String) -> Unit = { _, _, _ -> },
    deleteDraft: (Int) -> Unit = { _ -> },
    onBackPressed: () -> Unit = {}
) {
    val draft by draftLiveData.observeAsState(
        initial = QuestionDraft(0, "", "", "", "", "")
    )
    var title by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = draft.title))
    }
    var body by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = draft.body))
    }
    var tags by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = draft.tags))
    }
    var isPreview by rememberSaveable { mutableStateOf(BuildConfig.DEBUG) }

    fun isValidTitle() = title.text.isNotBlank() && title.text.length >= MIN_TITLE_LENGTH
    fun isValidBody() = body.text.isNotBlank() && body.text.length >= MIN_BODY_LENGTH
    fun isValidTags() = tags.text.isNotBlank() && tags.text.split(",").isNotEmpty()

    StackTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = stringResource(R.string.ask_question)) },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        if (listOf(title.text, body.text, tags.text).any { it.isNotBlank() }) {
                            IconButton(
                                onClick = { saveDraft(title.text, body.text, tags.text) },
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_baseline_save),
                                    contentDescription = null,
                                )
                            }
                            IconButton(
                                onClick = {
                                    title = TextFieldValue()
                                    body = TextFieldValue()
                                    tags = TextFieldValue()
                                    isPreview = false
                                    deleteDraft(draft.id)
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = null,
                                )
                            }
                        }
                    })
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
                            askQuestion(
                                title.text,
                                body.text,
                                tags.text,
                                isPreview
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_send),
                                contentDescription = null,
                                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                            )
                        },
                    )
                }
            },
        ) {
            Box(modifier = Modifier.padding(it)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(text = stringResource(R.string.title)) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isValidTitle(),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = body,
                        onValueChange = { body = it },
                        label = { Text(text = stringResource(R.string.body)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.body_height)),
                        isError = !isValidBody(),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = tags,
                        onValueChange = { tags = it },
                        label = { Text(text = stringResource(R.string.tags_title)) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isValidTags(),
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
    }
}
