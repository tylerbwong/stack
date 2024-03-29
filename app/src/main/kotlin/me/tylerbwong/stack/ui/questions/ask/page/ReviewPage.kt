package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.ask.AskQuestionState
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel
import me.tylerbwong.stack.ui.utils.compose.MarkdownText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReviewPage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    val askQuestionState by viewModel.askQuestionState.observeAsState(
        initial = AskQuestionState.Idle,
    )
    AskQuestionDetailsLayout(
        title = stringResource(R.string.review_page_title),
        description = stringResource(R.string.review_page_description),
    ) {
        AnimatedVisibility(visible = askQuestionState is AskQuestionState.Error) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                ),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.review_page_error),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = (askQuestionState as? AskQuestionState.Error)?.errorMessage
                        ?: stringResource(R.string.review_page_error_unknown),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = viewModel.title.trim(),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            MarkdownText(
                markdown = viewModel.fullBodyText,
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
                    horizontalArrangement = spacedBy(8.dp),
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
