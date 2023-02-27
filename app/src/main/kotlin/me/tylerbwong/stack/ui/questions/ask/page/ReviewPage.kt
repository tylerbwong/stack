package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
        ) {
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewModel.title.trim(),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(16.dp))
                MarkdownText(
                    markdown = listOf(viewModel.body, viewModel.expandBody)
                        .joinToString("\r\n\r\n")
                        .trim(),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(visible = viewModel.selectedTags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        mainAxisSpacing = 8.dp,
                    ) {
                        viewModel.selectedTags.forEach {
                            InputChip(
                                selected = false,
                                onClick = {},
                                label = { Text(text = it.name) },
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
