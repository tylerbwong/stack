@file:Suppress("LongMethod")

package me.tylerbwong.stack.ui.questions.ask

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.service.DEFAULT_SITE
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.compose.LabeledCheckbox
import me.tylerbwong.stack.ui.utils.compose.StackTheme
import me.tylerbwong.stack.ui.utils.compose.TextFormatToolbar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun AskQuestionLayout(onFinish: () -> Unit) {
    val viewModel = viewModel<AskQuestionViewModel>()
    val currentSiteParameter by viewModel.currentSiteParameter.observeAsState()
    val isDetailedQuestionRequired by remember {
        derivedStateOf { currentSiteParameter == DEFAULT_SITE }
    }
    val askQuestionState by viewModel.askQuestionState.observeAsState(
        initial = AskQuestionState.Idle,
    )
    val pagerState = rememberPagerState(
        initialPage = if (viewModel.hasActiveDraft) {
            AskQuestionPage.Title
        } else {
            AskQuestionPage.Start
        }.ordinal
    )
    LaunchedEffect(askQuestionState) {
        if (askQuestionState is AskQuestionState.Success || askQuestionState == AskQuestionState.SuccessPreview) {
            pagerState.animateScrollToPage(AskQuestionPage.Success.ordinal)
        } else if (askQuestionState == AskQuestionState.DraftDeleted) {
            pagerState.animateScrollToPage(AskQuestionPage.Start.ordinal)
        }
    }
    val draftStatus by viewModel.draftStatus.observeAsState(initial = DraftStatus.None)
    val currentPage by remember {
        derivedStateOf { AskQuestionPage.getCurrentPage(pagerState.currentPage) }
    }
    val isDuplicateQuestionPage by remember {
        derivedStateOf { currentPage == AskQuestionPage.DuplicateQuestion }
    }
    val isDetailsPage by remember {
        derivedStateOf { currentPage == AskQuestionPage.Details }
    }
    val isExpandDetailsPage by remember {
        derivedStateOf { currentPage == AskQuestionPage.ExpandDetails }
    }
    val similarQuestions by viewModel.similarQuestions.observeAsState(initial = emptyList())
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    StackTheme {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .navigationBarsPadding()
                .imePadding(),
            topBar = {
                TopAppBar(
                    title = {
                        AnimatedVisibility(
                            visible = draftStatus == DraftStatus.Saving,
                            enter = fadeIn() + slideInHorizontally(),
                            exit = slideOutHorizontally { it / 2 } + fadeOut(),
                        ) {
                            Text(text = stringResource(R.string.draft_status_saving))
                        }
                        AnimatedVisibility(
                            visible = draftStatus == DraftStatus.Complete,
                            enter = fadeIn() + slideInHorizontally(),
                            exit = slideOutHorizontally { it / 2 } + fadeOut(),
                        ) {
                            Text(text = stringResource(R.string.draft_status_saved))
                        }
                        AnimatedVisibility(
                            visible = draftStatus == DraftStatus.Failed,
                            enter = fadeIn() + slideInHorizontally(),
                            exit = slideOutHorizontally { it / 2 } + fadeOut(),
                        ) {
                            Text(text = stringResource(R.string.draft_status_failed))
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onFinish) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        var isDeleteDraftDialogVisible by remember { mutableStateOf(false) }
                        AnimatedVisibility(
                            visible = viewModel.showDeleteIcon,
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            IconButton(onClick = { isDeleteDraftDialogVisible = true }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = null,
                                )
                            }
                        }
                        if (isDeleteDraftDialogVisible) {
                            AlertDialog(
                                onDismissRequest = { isDeleteDraftDialogVisible = false },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            viewModel.deleteDraft()
                                            isDeleteDraftDialogVisible = false
                                        },
                                    ) {
                                        Text(text = stringResource(R.string.confirm))
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { isDeleteDraftDialogVisible = false },
                                    ) {
                                        Text(text = stringResource(R.string.cancel))
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = null,
                                    )
                                },
                                title = { Text(text = stringResource(R.string.delete_draft)) },
                                text = {
                                    Text(text = stringResource(R.string.delete_draft_summary))
                                },
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            bottomBar = {
                Column {
                    AnimatedVisibility(
                        visible = isDuplicateQuestionPage && similarQuestions.isNotEmpty(),
                        enter = fadeIn() + slideInVertically { it / 2 },
                        exit = slideOutVertically { it / 2 } + fadeOut(),
                    ) {
                        LabeledCheckbox(
                            label = stringResource(R.string.confirm_existing_posts),
                            checked = viewModel.isReviewed,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onCheckedChange = { viewModel.isReviewed = it },
                        )
                    }
                    AnimatedVisibility(
                        visible = isDetailsPage || isExpandDetailsPage,
                        enter = fadeIn() + slideInVertically { it / 2 },
                        exit = slideOutVertically { it / 2 } + fadeOut(),
                    ) {
                        TextFormatToolbar(
                            value = if (isDetailsPage) {
                                viewModel.body
                            } else if (isExpandDetailsPage) {
                                viewModel.expandBody
                            } else {
                                TextFieldValue()
                            },
                            onValueChange = {
                                if (isDetailsPage) {
                                    viewModel.updateBody(it)
                                } else if (isExpandDetailsPage) {
                                    viewModel.updateExpandBody(it)
                                }
                            },
                        )
                    }
                    BottomNavigationBar(
                        state = pagerState,
                        askQuestionState = askQuestionState,
                        page = currentPage,
                        isDetailedQuestionRequired = isDetailedQuestionRequired,
                        onFinish = onFinish,
                    )
                }
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                AnimatedVisibility(visible = askQuestionState == AskQuestionState.Posting) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                HorizontalPager(
                    count = AskQuestionPage.values().size,
                    state = pagerState,
                    userScrollEnabled = false,
                ) { page ->
                    AskQuestionPage.getCurrentPage(page)?.page?.invoke(isDetailedQuestionRequired)
                }
            }
        }
    }
}

@Suppress("ComplexMethod") // TODO Break this component up
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BottomNavigationBar(
    state: PagerState,
    askQuestionState: AskQuestionState,
    page: AskQuestionPage<*>?,
    isDetailedQuestionRequired: Boolean,
    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel = viewModel<AskQuestionViewModel>()
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            onClick = {
                if (page !in listOf(AskQuestionPage.Start, AskQuestionPage.Success)) {
                    scope.launch {
                        val previousPage = (state.currentPage - 1).coerceAtLeast(0)
                        state.animateScrollToPage(previousPage)
                    }
                } else {
                    onFinish()
                }
            },
            modifier = Modifier.padding(16.dp),
            enabled = askQuestionState != AskQuestionState.Posting,
        ) {
            Text(
                text = stringResource(
                    if (page in listOf(AskQuestionPage.Start, AskQuestionPage.Success)) {
                        R.string.exit
                    } else {
                        R.string.back
                    }
                )
            )
        }
        Button(
            onClick = {
                when (page) {
                    AskQuestionPage.Review -> viewModel.askQuestion()
                    else -> when (askQuestionState) {
                        is AskQuestionState.Success -> {
                            QuestionDetailActivity.startActivity(
                                context,
                                askQuestionState.questionId
                            )
                            onFinish()
                        }
                        AskQuestionState.SuccessPreview -> {
                            onFinish()
                        }
                        else -> {
                            scope.launch {
                                val nextPage =
                                    (state.currentPage + 1).coerceAtMost(state.pageCount - 1)
                                state.animateScrollToPage(nextPage)
                            }
                        }
                    }
                }
            },
            modifier = Modifier.padding(16.dp),
            enabled = when (page) {
                is AskQuestionPage.Title -> page.canContinue(
                    viewModel.title,
                    isDetailedQuestionRequired
                )
                is AskQuestionPage.Details -> page.canContinue(
                    viewModel.body.text,
                    isDetailedQuestionRequired
                )
                is AskQuestionPage.ExpandDetails -> page.canContinue(
                    viewModel.expandBody.text,
                    isDetailedQuestionRequired
                )
                is AskQuestionPage.Tags -> page.canContinue(
                    viewModel.selectedTags,
                    isDetailedQuestionRequired
                )
                is AskQuestionPage.DuplicateQuestion -> page.canContinue(
                    viewModel.isReviewed,
                    isDetailedQuestionRequired
                )
                else -> askQuestionState != AskQuestionState.Posting
            },
        ) {
            Text(
                text = stringResource(
                    when (page) {
                        AskQuestionPage.DuplicateQuestion -> R.string.review
                        AskQuestionPage.Review -> R.string.post
                        else -> if (askQuestionState is AskQuestionState.Success ||
                            askQuestionState == AskQuestionState.SuccessPreview
                        ) {
                            R.string.view
                        } else {
                            R.string.next
                        }
                    }
                )
            )
        }
    }
}
