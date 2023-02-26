package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Title.TITLE_LENGTH_LIMIT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitlePage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    AskQuestionDetailsLayout(
        title = "Title",
        description = "Be specific and imagine you're asking a question to another person.",
    ) {
        OutlinedTextField(
            value = viewModel.title,
            onValueChange = {
                if (it.length <= TITLE_LENGTH_LIMIT) {
                    viewModel.title = it
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = "Summarize the problem here") },
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "${viewModel.title.length} / $TITLE_LENGTH_LIMIT",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}
