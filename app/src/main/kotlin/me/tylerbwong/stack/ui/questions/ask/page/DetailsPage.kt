package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Details.MIN_DETAILS_LENGTH

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsPage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    LaunchedEffect(viewModel.body) {
        if (viewModel.shouldSaveDraft && viewModel.body.isNotBlank()) {
            delay(1_000)
            viewModel.saveDraft()
        }
    }
    AskQuestionDetailsLayout(
        title = "What are the details of your problem?",
        description = "Introduce the problem and expand on what you put in the title. Minimum $MIN_DETAILS_LENGTH characters.",
    ) {
        OutlinedTextField(
            value = viewModel.body,
            onValueChange = { viewModel.body = it },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            placeholder = { Text(text = "Explain how you encountered the problem") },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandDetailsPage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    LaunchedEffect(viewModel.expandBody) {
        if (viewModel.shouldSaveDraft && viewModel.expandBody.isNotBlank()) {
            delay(1_000)
            viewModel.saveDraft()
        }
    }
    AskQuestionDetailsLayout(
        title = "What did you try?",
        description = "Describe what you tried, what you expected, and what actually resulted. Minimum $MIN_DETAILS_LENGTH characters.",
    ) {
        OutlinedTextField(
            value = viewModel.expandBody,
            onValueChange = { viewModel.expandBody = it },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            placeholder = { Text(text = "Expand on the problem") },
        )
    }
}
