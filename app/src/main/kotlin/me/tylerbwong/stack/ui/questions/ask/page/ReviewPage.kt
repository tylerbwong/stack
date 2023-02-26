package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.flowlayout.FlowRow
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel
import me.tylerbwong.stack.ui.utils.compose.MarkdownText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewPage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    AskQuestionDetailsLayout(
        title = "Review your question",
        description = "Please do a final review of your question and then post.",
    ) {
        Text(
            text = viewModel.title,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        MarkdownText(
            markdown = listOf(viewModel.details, viewModel.expandDetails)
                .joinToString("\n"),
            modifier = Modifier.fillMaxWidth(),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(visible = viewModel.selectedTags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))
            FlowRow(mainAxisSpacing = 8.dp) {
                viewModel.selectedTags.forEach {
                    InputChip(
                        selected = false,
                        onClick = {},
                        label = { Text(text = it.name) },
                    )
                }
            }
        }
    }
}
