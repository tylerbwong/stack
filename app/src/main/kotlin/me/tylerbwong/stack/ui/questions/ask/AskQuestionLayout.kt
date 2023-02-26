@file:Suppress("LongMethod")

package me.tylerbwong.stack.ui.questions.ask

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage
import me.tylerbwong.stack.ui.utils.compose.LabeledCheckbox
import me.tylerbwong.stack.ui.utils.compose.StackTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun AskQuestionLayout(onFinish: () -> Unit) {
    val pagerState = rememberPagerState()
    val viewModel = viewModel<AskQuestionViewModel>()
    val currentPage by remember {
        derivedStateOf { AskQuestionPage.getCurrentPage(pagerState.currentPage) }
    }
    val isDuplicateQuestionPage by remember {
        derivedStateOf { currentPage == AskQuestionPage.DuplicateQuestion }
    }
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
            bottomBar = {
                Column {
                    AnimatedVisibility(visible = isDuplicateQuestionPage) {
                        LabeledCheckbox(
                            label = "I confirm that none of these existing posts answers my question.",
                            checked = viewModel.isReviewed,
                            onCheckedChange = { viewModel.isReviewed = it },
                        )
                    }
                    BottomNavigationBar(
                        state = pagerState,
                        page = currentPage,
                        onFinish = onFinish,
                    )
                }
            }
        ) {
            HorizontalPager(
                count = AskQuestionPage.values().size,
                modifier = Modifier.padding(it),
                state = pagerState,
                userScrollEnabled = false,
            ) { page -> AskQuestionPage.getCurrentPage(page)?.page?.invoke() }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BottomNavigationBar(
    state: PagerState,
    page: AskQuestionPage<*>?,
    onFinish: () -> Unit,
) {
    val viewModel = viewModel<AskQuestionViewModel>()
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            onClick = {
                if (page != AskQuestionPage.Start) {
                    scope.launch {
                        val previousPage = (state.currentPage - 1).coerceAtLeast(0)
                        state.animateScrollToPage(previousPage)
                    }
                } else {
                    onFinish()
                }
            },
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = if (page == AskQuestionPage.Start) "Exit" else "Back")
        }
        Button(
            onClick = {
                when (page) {
                    AskQuestionPage.Review -> viewModel.askQuestion()
                    else -> {
                        scope.launch {
                            val nextPage = (state.currentPage + 1).coerceAtMost(state.pageCount - 1)
                            state.animateScrollToPage(nextPage)
                        }
                    }
                }
            },
            modifier = Modifier.padding(16.dp),
            enabled = when (page) {
                is AskQuestionPage.Title -> page.canContinue(viewModel.title)
                is AskQuestionPage.Details -> page.canContinue(viewModel.details)
                is AskQuestionPage.ExpandDetails -> page.canContinue(viewModel.expandDetails)
                is AskQuestionPage.Tags -> page.canContinue(viewModel.selectedTags)
                is AskQuestionPage.DuplicateQuestion -> page.canContinue(viewModel.isReviewed)
                else -> true
            },
        ) {
            Text(
                text = when (page) {
                    AskQuestionPage.DuplicateQuestion -> "Review"
                    AskQuestionPage.Review -> "Post"
                    else -> "Next"
                }
            )
        }
    }
}
