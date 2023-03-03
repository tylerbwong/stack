package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Details.MIN_DETAILS_LENGTH
import me.tylerbwong.stack.ui.utils.compose.TextFormatToolbar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DetailsPage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    val requester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    AskQuestionDetailsLayout(
        title = stringResource(R.string.details_page_title),
        description = stringResource(R.string.details_page_description, MIN_DETAILS_LENGTH),
    ) {
        TextFormatToolbar(
            value = viewModel.body,
            onValueChange = viewModel::updateBody,
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.body,
            onValueChange = viewModel::updateBody,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .bringIntoViewRequester(requester)
                .onFocusChanged {
                    if (it.isFocused) {
                        scope.launch {
                            delay(200)
                            requester.bringIntoView()
                        }
                    }
                },
            placeholder = { Text(text = stringResource(R.string.details_page_hint)) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpandDetailsPage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    val requester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    AskQuestionDetailsLayout(
        title = stringResource(R.string.expand_details_page_title),
        description = stringResource(R.string.expand_details_page_description, MIN_DETAILS_LENGTH),
    ) {
        TextFormatToolbar(
            value = viewModel.expandBody,
            onValueChange = viewModel::updateExpandBody,
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.expandBody,
            onValueChange = viewModel::updateExpandBody,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .bringIntoViewRequester(requester)
                .onFocusChanged {
                    if (it.isFocused) {
                        scope.launch {
                            delay(200)
                            requester.bringIntoView()
                        }
                    }
                },
            placeholder = { Text(text = stringResource(R.string.expand_details_hint)) },
        )
    }
}
